package da2i.payetesdettes.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Aspect
@Component
public class LoggerUtil {
	
	Logger logger = LoggerFactory.getLogger("LOG :");
	LocalDateTime date = null;
	
	@Pointcut(value="execution(* da2i.payetesdettes.services..*(..)))")
	public void servicesPointCut() {}
	
	@Pointcut(value="execution(* da2i.payetesdettes.controllers..*(..)))")
	public void controllersPointCut() {}
	
	@Pointcut(value="execution(* da2i.payetesdettes.repositories..*(..)))")
	public void repositoriesPointCut() {}
	
	@Pointcut("servicesPointCut() || controllersPointCut() || repositoriesPointCut()")
	public void allPointCut() {}
	
	@Around("allPointCut()")
	public Object generateLog (ProceedingJoinPoint pjp) throws Throwable {
		initLogsFolder();
		
		ObjectMapper mapper = new ObjectMapper();
		String callMessage = "";
		String responseMessage = "";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		DateTimeFormatter fileFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss");
		if (date == null) this.date = LocalDateTime.now();
		
		String nomClasse = pjp.getTarget().getClass().getName();
		String nomMethode = pjp.getSignature().getName();
		Object responseObj = null;
		
		responseObj = pjp.proceed();
		
		String dateNow = LocalDateTime.now().format(formatter);
		
		try {
			Object[] callArgs = pjp.getArgs();
			callArgs = filterNull(callArgs);
			callMessage = dateNow + " || Méthode appelée : "+nomClasse+" : "+nomMethode+"() param :"+mapper.writeValueAsString(callArgs);
			logger.info(callMessage);
		} catch (Exception e) {
			//e.printStackTrace();
			callMessage = dateNow + " || Méthode appelée : "+nomClasse+" : "+nomMethode+"() param : indisponible";
			logger.info(callMessage);
		}
		
		dateNow = LocalDateTime.now().format(formatter);
		
		try {
			if(responseObj!=null) {
				responseMessage = dateNow + " || Réponse de l'appel "+nomClasse+" : "+nomMethode+"() a retourné : "+mapper.writeValueAsString(responseObj);
			} else {
				responseMessage = dateNow + " || Réponse de l'appel "+nomClasse+" : "+nomMethode+"() a retourné : indisponible";
			}
			
			logger.info(responseMessage);
		} catch (JsonProcessingException e) {
			//e.printStackTrace();
			responseMessage = dateNow + " || Réponse de l'appel "+nomClasse+" : "+nomMethode+"() a retourné : indisponible";
			logger.info(responseMessage);
		}
		
		try (FileWriter myWriter = new FileWriter("logs/"+date.format(fileFormatter)+".txt",true)) {
			myWriter.write(callMessage+"\n");
			myWriter.write(responseMessage+"\n");
		} catch (IOException e) {
			System.out.println("Une erreur est survenue dans l'écriture des logs");
			e.printStackTrace();
		}
		
		return responseObj;
	}
	
	public Object[] filterNull (Object[] objTab) throws IllegalArgumentException, IllegalAccessException {
		List<Object> result = new ArrayList<>();
		for (Object obj : objTab) {
			//System.out.println("------------- OBJ : "+obj.getClass()+"--------------------------");
			boolean nullFlag = false;
			for (Field f : obj.getClass().getDeclaredFields()) {
				f.setAccessible(true);
				//System.out.println("Field : "+f);
				//System.out.println("Value : "+f.get(obj));
				if (f.get(obj) == null) nullFlag = true;
			}
			if (!nullFlag) {
				//System.out.println("add");
				result.add(obj);
			}
		}
		return result.toArray();
	}
	
	public void initLogsFolder () {
		new File("./logs/").mkdirs();
	}

}
