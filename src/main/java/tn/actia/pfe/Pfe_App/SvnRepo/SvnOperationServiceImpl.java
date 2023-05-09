package tn.actia.pfe.Pfe_App.SvnRepo;

import org.springframework.stereotype.Service;
import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.*;

import java.io.File;

@Service
public class SvnOperationServiceImpl implements SvnOperationService{
    @Override
    public String addFolder(String workingCopyPath, String folderName) {
        File workingCopyDir = new File(workingCopyPath);
        File newFolder = new File(workingCopyDir, folderName);

        if (newFolder.exists()) {
            return "Le dossier existe déjà.";
        }

        if (!newFolder.mkdir()) {
            return "Erreur lors de la création du dossier.";
        }

        SVNClientManager clientManager = SVNClientManager.newInstance();
        SVNWCClient wcClient = clientManager.getWCClient();

        try {
            wcClient.doAdd(newFolder, false, false, false, SVNDepth.INFINITY, false, false);
            return "Dossier ajouté avec succès.";
        } catch (SVNException e) {
            return "Erreur lors de l'ajout du dossier : " + e.getMessage();
        }
    }

    @Override
    public String checkout(String url, String destinationPath) {
        SVNURL svnurl;
        try {
            svnurl = SVNURL.parseURIEncoded(url);
        } catch (SVNException e) {
            return "Erreur lors de l'analyse de l'URL : " + e.getMessage();
        }

        File destinationDir = new File(destinationPath);
        SVNUpdateClient updateClient = new SVNUpdateClient(SVNWCUtil.createDefaultAuthenticationManager(), SVNWCUtil.createDefaultOptions(true));
        try {
            updateClient.doCheckout(svnurl, destinationDir, SVNRevision.HEAD, SVNRevision.HEAD, SVNDepth.INFINITY, true);
            return "Checkout réussi.";
        } catch (SVNException e) {
            return "Erreur lors du checkout : " + e.getMessage();
        }
    }

    @Override
    public String commit(String workingCopyPath, String message) {
        File workingCopyDir = new File(workingCopyPath);
        SVNClientManager clientManager = SVNClientManager.newInstance();
        SVNCommitClient commitClient = clientManager.getCommitClient();
        try {
            SVNCommitInfo commitInfo = commitClient.doCommit(new File[]{workingCopyDir}, true, message, null, null, false, false, SVNDepth.INFINITY);
            return "Commit réussi. Révision : " + commitInfo.getNewRevision();
        } catch (SVNException e) {
            return "Erreur lors du commit : " + e.getMessage();
        }
    }

    @Override
    public String getRepositoryInfo(String url) {
        SVNURL svnurl;
        try {
            svnurl = SVNURL.parseURIEncoded(url);
        } catch (SVNException e) {
            return "Erreur lors de l'analyse de l'URL : " + e.getMessage();
        }

        SVNRepositoryFactoryImpl.setup();
        SVNRepository repository;
        try {
            repository = SVNRepositoryFactory.create(svnurl);
        } catch (SVNException e) {
            return "Erreur lors de la création du référentiel : " + e.getMessage();
        }

        SVNNodeKind nodeKind;
        try {
            nodeKind = repository.checkPath("", -1);
        } catch (SVNException e) {
            return "Erreur lors de la vérification du chemin : " + e.getMessage();
        }

        if (nodeKind == SVNNodeKind.NONE) {
            return "Aucun référentiel trouvé à l'URL spécifiée.";
        } else if (nodeKind == SVNNodeKind.FILE) {
            return "L'URL spécifiée correspond à un fichier, pas à un référentiel.";
        }

        try {
            long latestRevision = repository.getLatestRevision();
            return "URL du référentiel: " + url + "\nDernière révision : " + latestRevision;
        } catch (SVNException e) {
            return "Erreur lors de la récupération des informations du référentiel : " + e.getMessage();
        }
    }
    @Override
    public String addDefaultFolders(String workingCopyPath) {
        String[] folderNames = {"branches", "tags", "trunk"};

        for (String folderName : folderNames) {
            String result = addFolder(workingCopyPath, folderName);

            if (!result.contains("avec succès")) {
                return "Erreur lors de la création du dossier " + folderName + ": " + result;
            }
        }

        return "Dossiers branches, tags et trunk ajoutés avec succès.";
    }
}


