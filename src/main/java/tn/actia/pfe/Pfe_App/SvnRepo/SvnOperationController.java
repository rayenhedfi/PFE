package tn.actia.pfe.Pfe_App.SvnRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/svn")

public class SvnOperationController {

    @Autowired
    private SvnOperationService svnOperationService;

    @GetMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestParam String url, @RequestParam String destinationPath) {
        String result = svnOperationService.checkout(url, destinationPath);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/commit")
    public ResponseEntity<String> commit(@RequestParam String workingCopyPath, @RequestParam String message) {
        String result = svnOperationService.commit(workingCopyPath, message);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/info")
    public ResponseEntity<String> getRepositoryInfo(@RequestParam String url) {
        String result = svnOperationService.getRepositoryInfo(url);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/add-folder")
    public ResponseEntity<String> addFolder(@RequestParam String workingCopyPath, @RequestParam String folderName) {
        String result = svnOperationService.addFolder(workingCopyPath, folderName);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/svn/add-default-folders")
    public ResponseEntity<String> addDefaultFolders(@RequestParam String workingCopyPath) {
        String result = svnOperationService.addDefaultFolders(workingCopyPath);
        return ResponseEntity.ok(result);
    }
}
