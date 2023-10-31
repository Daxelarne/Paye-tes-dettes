package da2i.payetesdettes.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

public class UploadDeleteFile {
	/**
	 * Permet de sauvegarder un fichier envoyé depuis un formulaire
	 * @param uploadDir
	 * @param fileName
	 * @param multipartFile
	 * @throws IOException
	 */
	public static void saveFile (String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
         
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
         
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {        
            throw new IOException("Could not save image file: " + fileName, ioe);
        }  
      
    }
	
	/**
	 * Permet de détruire un dossier et son contenu. Utile pour les images de profils ou les images d'un event
	 * @param uploadDir
	 * @throws IOException
	 */
	public static void deleteAllFilesAndDirectory (String uploadDir) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        
        try {
        	FileUtils.cleanDirectory(new File(uploadDir)); 
        	
        	Files.deleteIfExists(uploadPath);
        } catch(IOException e) {
        	e.printStackTrace();
        	throw e;
        }
    }
	
	/**
	 * Permet de supprimer un fichier
	 * @param filePath Chemin du fichier
	 * @throws IOException
	 */
	public static void deleteFile (String filePath) {
    	File f= new File(filePath);
    	
    	f.delete();
    }
	
	
}
