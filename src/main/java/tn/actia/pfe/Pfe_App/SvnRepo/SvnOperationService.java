package tn.actia.pfe.Pfe_App.SvnRepo;

public interface SvnOperationService {
    String addFolder(String workingCopyPath, String folderName);
    String checkout(String url, String destinationPath);
    String commit(String workingCopyPath, String message);
    String getRepositoryInfo(String url);
    String addDefaultFolders(String workingCopyPath);


}
