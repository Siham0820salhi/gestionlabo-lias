package com.lias.gestionlabo.services;

import com.lias.gestionlabo.models.DemandeMateriel;
import com.lias.gestionlabo.models.DistributionMateriel;
import com.lias.gestionlabo.models.Materiel;
import com.lias.gestionlabo.models.Membre;
import com.lias.gestionlabo.models.RapportAnnuel;
import com.lias.gestionlabo.models.StatutDemande;
import com.lias.gestionlabo.models.StatutMembre;
import com.lias.gestionlabo.models.TypePret;
import com.lias.gestionlabo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class MaterielService {

    @Autowired private DemandeMaterielRepository  demandeRepo;
    @Autowired private DistributionMaterielRepository distRepo;
    @Autowired private MaterielRepository            materielRepo;
    @Autowired private RapportAnnuelRepository       rapportRepo;
    @Autowired private MembreRepository              membreRepo;
    @Autowired private EvenementRepository           evenementRepo;
    @Autowired private PublicationRepository         publicationRepo;

    // ✅ Accepter demande
    @Transactional
    public void accepterDemande(Long idDemande) {
        DemandeMateriel demande = demandeRepo.findById(idDemande)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));

        // ① Statut → ACCEPTEE
        demande.setStatut(StatutDemande.ACCEPTEE);
        demandeRepo.save(demande);

        // ② Créer distribution automatiquement avec description copiée
        DistributionMateriel distribution = new DistributionMateriel();
        distribution.setDemande(demande);
        distribution.setMembre(demande.getMembre());
        distribution.setDateAcceptation(LocalDateTime.now());

        // ✅ Kopier description o infos membre
        distribution.setDescriptionDemande(demande.getDescription());
        distribution.setNomMembre(demande.getMembre().getNom());
        distribution.setPrenomMembre(demande.getMembre().getPrenom());
        distribution.setDepartementMembre(demande.getDepartement());
        distribution.setFiliereMembre(demande.getFiliere());
        // Copier le type de prêt et les dates de la demande
        if (demande.getTypePret() != null) distribution.setTypePret(demande.getTypePret());
        distribution.setDateDebut(demande.getDateDebutSouhaitee() != null
                ? demande.getDateDebutSouhaitee() : LocalDate.now());
        distribution.setDateFin(demande.getDateFinSouhaitee());

        distRepo.save(distribution);
    }

    // ── Distribution directe par le Directeur ──────────────────────────
    @Transactional
    public void distribuerDirectement(Long materielId, Long membreId,
                                      TypePret typePret,
                                      LocalDate dateDebut, LocalDate dateFin) {
        Materiel mat = materielRepo.findById(materielId)
                .orElseThrow(() -> new RuntimeException("Matériel introuvable"));
        Membre mem = membreRepo.findById(membreId)
                .orElseThrow(() -> new RuntimeException("Membre introuvable"));

        DistributionMateriel d = new DistributionMateriel();
        d.setMateriel(mat);
        d.setMembre(mem);
        d.setTypePret(typePret);
        d.setDateDebut(dateDebut != null ? dateDebut : LocalDate.now());
        d.setDateFin(typePret == TypePret.DEFINITIF ? null : dateFin);
        d.setDateAcceptation(LocalDateTime.now());
        d.setNomMembre(mem.getNom());
        d.setPrenomMembre(mem.getPrenom());
        d.setDescriptionDemande(mat.getNom() + " — " + mat.getReference());
        distRepo.save(d);
    }

    // ── Marquer un matériel comme retourné ──────────────────────────────
    @Transactional
    public void marquerRetourné(Long distributionId) {
        DistributionMateriel d = distRepo.findById(distributionId)
                .orElseThrow(() -> new RuntimeException("Distribution introuvable"));
        d.setActif(false);
        distRepo.save(d);
    }

    // ✅ Refuser demande
    @Transactional
    public void refuserDemande(Long idDemande) {
        DemandeMateriel demande = demandeRepo.findById(idDemande)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));

        demande.setStatut(StatutDemande.REFUSEE);
        demandeRepo.save(demande);
    }

    // Générer et sauvegarder rapport complet
    @Transactional
    public RapportAnnuel genererRapportAnnuel(int annee) {
        RapportAnnuel rapport = buildRapportData(annee);
        return rapportRepo.save(rapport);
    }

    private RapportAnnuel buildRapportData(int annee) {
        // ── Matériel ────────────────────────────
        List<DemandeMateriel> toutesLesDemandes = demandeRepo.findAll();

        long acceptes = toutesLesDemandes.stream()
            .filter(d -> d.getStatut() == StatutDemande.ACCEPTEE
                      && d.getDateDemande() != null
                      && d.getDateDemande().getYear() == annee)
            .count();

        long refuses = toutesLesDemandes.stream()
            .filter(d -> d.getStatut() == StatutDemande.REFUSEE
                      && d.getDateDemande() != null
                      && d.getDateDemande().getYear() == annee)
            .count();

        List<DistributionMateriel> distributions = distRepo.findAll().stream()
            .filter(dist -> dist.getDateAcceptation() != null
                         && dist.getDateAcceptation().getYear() == annee)
            .collect(Collectors.toList());

        // ── Membres ─────────────────────────────
        long membresActifs = membreRepo.findAll().stream()
            .filter(m -> m.getStatutActuel() != null
                      && m.getStatutActuel() != StatutMembre.RETRAITE
                      && m.getStatutActuel() != StatutMembre.ANCIEN)
            .count();

        long nouveauxMembres = membreRepo.findAll().stream()
            .filter(m -> m.getDateAffiliation() != null
                      && (m.getDateAffiliation().getYear() + 1900) == annee)
            .count();

        // ── Publications ─────────────────────────
        long totalPubs = publicationRepo.findAll().stream()
            .filter(p -> p.getAnnee() == annee)
            .count();

        // ── Événements ───────────────────────────
        long totalEv = evenementRepo.findByDateYear(annee).size();

        RapportAnnuel rapport = new RapportAnnuel();
        rapport.setAnnee(annee);
        rapport.setDateGeneration(LocalDateTime.now());
        rapport.setTotalAccepte((int) acceptes);
        rapport.setTotalRefuse((int) refuses);
        rapport.setDistributions(distributions);
        rapport.setTotalMembresActifs((int) membresActifs);
        rapport.setNouveauxMembres((int) nouveauxMembres);
        rapport.setTotalPublications((int) totalPubs);
        rapport.setTotalEvenements((int) totalEv);
        return rapport;
    }

    // Générer PDF rapport annuel complet
    public void genererPdfRapport(int annee, HttpServletResponse response)
            throws IOException, DocumentException {

        Document document = new Document(PageSize.A4, 50, 50, 60, 60);
        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font fontH1    = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22);
        Font fontH2    = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Font fontNormal= FontFactory.getFont(FontFactory.HELVETICA, 11);
        Font fontBold  = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
        Font fontMuted = FontFactory.getFont(FontFactory.HELVETICA, 10);
        fontMuted.setColor(100, 100, 100);

        // ─── En-tête ───────────────────────────────────────────────────────
        Paragraph title = new Paragraph("Rapport d'Activité Annuel " + annee, fontH1);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph lab = new Paragraph("Laboratoire LIAS — Faculté des Sciences Ben M'Sik, Casablanca", fontMuted);
        lab.setAlignment(Element.ALIGN_CENTER);
        document.add(lab);
        document.add(Chunk.NEWLINE);

        // ─── Charger données (sans sauvegarder en DB) ──────────────────────
        RapportAnnuel rapport = rapportRepo.findTopByAnneeOrderByDateGenerationDesc(annee)
            .orElseGet(() -> buildRapportData(annee));

        // ─── Section 1 : Membres ────────────────────────────────────────────
        document.add(new Paragraph("1. MEMBRES DU LABORATOIRE", fontH2));
        document.add(new Paragraph("Membres actifs (permanent, associé, doctorant)  : "
            + rapport.getTotalMembresActifs(), fontNormal));
        document.add(new Paragraph("Nouveaux affiliés en " + annee + "               : "
            + rapport.getNouveauxMembres(), fontNormal));
        document.add(Chunk.NEWLINE);

        // ─── Section 2 : Publications ───────────────────────────────────────
        document.add(new Paragraph("2. PRODUCTIONS SCIENTIFIQUES", fontH2));
        document.add(new Paragraph("Publications enregistrées en " + annee + "      : "
            + rapport.getTotalPublications(), fontNormal));

        List<com.lias.gestionlabo.models.Publication> pubs = publicationRepo.findAll().stream()
            .filter(p -> p.getAnnee() == annee)
            .collect(Collectors.toList());

        if (!pubs.isEmpty()) {
            document.add(Chunk.NEWLINE);
            PdfPTable tablePub = new PdfPTable(new float[]{4f, 3f, 2f});
            tablePub.setWidthPercentage(100);
            addTableHeader(tablePub, new String[]{"Titre", "Auteurs", "Journal / Conférence"}, fontBold);
            for (com.lias.gestionlabo.models.Publication p : pubs) {
                tablePub.addCell(cellOf(p.getTitre(), fontNormal));
                tablePub.addCell(cellOf(p.getAuteurs(), fontMuted));
                tablePub.addCell(cellOf(p.getJournalOuConference(), fontMuted));
            }
            document.add(tablePub);
        }
        document.add(Chunk.NEWLINE);

        // ─── Section 3 : Événements ─────────────────────────────────────────
        document.add(new Paragraph("3. ÉVÉNEMENTS SCIENTIFIQUES", fontH2));
        document.add(new Paragraph("Événements organisés en " + annee + "           : "
            + rapport.getTotalEvenements(), fontNormal));

        List<com.lias.gestionlabo.models.Evenement> evs = evenementRepo.findByDateYear(annee);
        if (!evs.isEmpty()) {
            document.add(Chunk.NEWLINE);
            PdfPTable tableEv = new PdfPTable(new float[]{3f, 2f, 2f, 2f});
            tableEv.setWidthPercentage(100);
            addTableHeader(tableEv, new String[]{"Titre", "Type", "Date", "Lieu"}, fontBold);
            for (com.lias.gestionlabo.models.Evenement e : evs) {
                tableEv.addCell(cellOf(e.getTitre(), fontNormal));
                tableEv.addCell(cellOf(e.getType() != null ? e.getType().name() : "—", fontMuted));
                tableEv.addCell(cellOf(e.getDate() != null ? e.getDate().toString() : "—", fontMuted));
                tableEv.addCell(cellOf(e.getLieu() != null ? e.getLieu() : "—", fontMuted));
            }
            document.add(tableEv);
        }
        document.add(Chunk.NEWLINE);

        // ─── Section 4 : Matériel ───────────────────────────────────────────
        document.add(new Paragraph("4. GESTION DU MATÉRIEL", fontH2));
        document.add(new Paragraph("Demandes acceptées en " + annee + "             : "
            + rapport.getTotalAccepte(), fontNormal));
        document.add(new Paragraph("Demandes refusées en  " + annee + "             : "
            + rapport.getTotalRefuse(), fontNormal));

        if (rapport.getDistributions() != null && !rapport.getDistributions().isEmpty()) {
            document.add(Chunk.NEWLINE);
            PdfPTable tableDist = new PdfPTable(new float[]{2f, 2f, 2f, 3f});
            tableDist.setWidthPercentage(100);
            addTableHeader(tableDist, new String[]{"Membre", "Département", "Filière", "Description"}, fontBold);
            for (DistributionMateriel dist : rapport.getDistributions()) {
                tableDist.addCell(cellOf(dist.getPrenomMembre() + " " + dist.getNomMembre(), fontNormal));
                tableDist.addCell(cellOf(dist.getDepartementMembre() != null ? dist.getDepartementMembre() : "—", fontMuted));
                tableDist.addCell(cellOf(dist.getFiliereMembre() != null ? dist.getFiliereMembre() : "—", fontMuted));
                tableDist.addCell(cellOf(dist.getDescriptionDemande() != null ? dist.getDescriptionDemande() : "—", fontMuted));
            }
            document.add(tableDist);
        }
        document.add(Chunk.NEWLINE);

        // ─── Pied de page ───────────────────────────────────────────────────
        Paragraph footer = new Paragraph(
            "Document généré le " + LocalDateTime.now().toString().substring(0, 16)
            + " — Laboratoire LIAS", fontMuted);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);

        document.close();
    }

    private void addTableHeader(PdfPTable table, String[] headers, Font font) {
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, font));
            cell.setBackgroundColor(new java.awt.Color(240, 240, 240));
            cell.setPadding(6);
            table.addCell(cell);
        }
    }

    private PdfPCell cellOf(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text != null ? text : "—", font));
        cell.setPadding(5);
        return cell;
    }
}