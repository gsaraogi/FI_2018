//package odi.sdk;
 
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
 
import oracle.odi.core.OdiInstance;
import oracle.odi.core.config.MasterRepositoryDbInfo;
import oracle.odi.core.config.OdiInstanceConfig;
import oracle.odi.core.config.PoolingAttributes;
import oracle.odi.core.config.WorkRepositoryDbInfo;
import oracle.odi.core.persistence.transaction.ITransactionStatus;
import oracle.odi.core.persistence.transaction.support.DefaultTransactionDefinition;
import oracle.odi.core.security.Authentication;
import oracle.odi.domain.project.IOdiScenarioSourceContainer;
import oracle.odi.domain.project.OdiFolder;
import oracle.odi.domain.project.OdiProject;
import oracle.odi.domain.project.finder.IOdiFolderFinder;
import oracle.odi.domain.project.finder.IOdiProjectFinder;
import oracle.odi.domain.project.finder.IOdiPackageFinder;
import oracle.odi.domain.mapping.finder.IMappingFinder;
import oracle.odi.domain.project.finder.IOdiUserProcedureFinder;
import oracle.odi.domain.runtime.scenario.OdiScenario;
import oracle.odi.domain.runtime.scenario.finder.IOdiScenarioFinder;
import oracle.odi.impexp.EncodingOptions;
import oracle.odi.impexp.OdiImportException;
import oracle.odi.impexp.support.ExportServiceImpl;
import oracle.odi.impexp.support.ImportServiceImpl;
 

    // private static String Project_Code;
     //private static OdiProject project;
    // private static String   Folder_Name;
    // private static OdiFolder folder;
 
      
    /****** Please change these Parameters *********/
 
    /** Development Repository ****/
 
     String Url = "jdbc:oracle:thin:@localhost:1521/pdborcl";
     String Driver="oracle.jdbc.OracleDriver";
     String Master_User="ODI_MASTER_REPO";
     String Master_Pass="ODI_MASTER_REPO";
     String WorkRep="WORKREP1";
     String Odi_User="SUPERVISOR";
     String Odi_Pass="security9";
 
     /*** Execution Repository ***/
     // In case if your Execution rep is linked to a different Master repository please appropriately create new variables
     // The present codes assumes that Development and Execution are linked to the same Master Repository.
 
    // String WorkRep_Execution="WORKREP_EXECUTION";
 
     /***************************************************/
 
     //Exporting Scenario Options
 
     String ExportFolderPath="C:\\Users\\gsaraogi\\Desktop\\ODI_EXP_OBJ";
 
     // While providing path in windows make sure you use 
    // Boolean ExportPackageScen      = true;
    // Boolean ExportInterfaceScen    = true;
    // Boolean ExportProcedureScen    = true;
    // Boolean ExportVariableScen     = false;
     Boolean RecursiveExport        = true;
     Boolean OverWriteFile          = true;
 
    // Project_Code   ="XMT";
    // Folder_Name    ="FOLDER2";
 
     // Date based on which the scenario is compared
     //SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
   //  Date date =df.parse("11-23-2011");
 
     /********************************************/
 
MasterRepositoryDbInfo masterInfo = new MasterRepositoryDbInfo(Url, Driver, Master_User,Master_Pass.toCharArray(), new PoolingAttributes());
 
/// Development Repository
WorkRepositoryDbInfo workInfo = new WorkRepositoryDbInfo(WorkRep, new PoolingAttributes());
OdiInstance odiInstance=OdiInstance.createInstance(new OdiInstanceConfig(masterInfo,workInfo));
Authentication auth = odiInstance.getSecurityManager().createAuthentication(Odi_User,Odi_Pass.toCharArray());
odiInstance.getSecurityManager().setCurrentThreadAuthentication(auth);
ITransactionStatus trans = odiInstance.getTransactionManager().getTransaction(new DefaultTransactionDefinition());
 
//Execution Repository
 
//WorkRepositoryDbInfo workInfo_exec = new WorkRepositoryDbInfo(WorkRep_Execution, new PoolingAttributes());
//OdiInstance odiInstance_exec=OdiInstance.createInstance(new OdiInstanceConfig(masterInfo,workInfo_exec));
//Authentication auth_exec = odiInstance_exec.getSecurityManager().createAuthentication(Odi_User,Odi_Pass.toCharArray());
//odiInstance_exec.getSecurityManager().setCurrentThreadAuthentication(auth_exec);
//ITransactionStatus trans_exec = odiInstance_exec.getTransactionManager().getTransaction(new DefaultTransactionDefinition());
 
         
        // Export Selected Scenario based on date
 
        ExportServiceImpl export=new ExportServiceImpl(odiInstance);
        EncodingOptions EncdOption = new EncodingOptions();
 
        //Reading all scenario
 
        Collection odiscen= ((IOdiScenarioFinder) odiInstance.getTransactionalEntityManager().getFinder(OdiScenario.class)).findAll();
        for (Object scen : odiscen) {
            OdiScenario odiscenario =(OdiScenario)scen ;
          //  if (odiscenario.getName() == "ETRM_ASX_PRICES_FILE_TO_LDS_MAP") {
                println(" Exporting scenario  "+odiscenario.getName());
                export.exportToXml(odiscenario, ExportFolderPath, OverWriteFile, RecursiveExport, EncdOption);
            //}
        }
		 //Reading all packages
		Collection odipkg= ((IOdiPackageFinder) odiInstance.getTransactionalEntityManager().getFinder(OdiPackage.class)).findAll();
		 for (Object pkg : odipkg) {
            OdiPackage odipackage =(OdiPackage)pkg ;
          //  if (odiscenario.getName() == "ETRM_ASX_PRICES_FILE_TO_LDS_MAP") {
                println(" Exporting package  "+odipackage.getName());
                export.exportToXml(odipackage, ExportFolderPath, OverWriteFile, RecursiveExport, EncdOption);
            //}
        }
		
		//Reading all mappings
		Collection odimapping= ((IMappingFinder) odiInstance.getTransactionalEntityManager().getFinder(Mapping.class)).findAll();
		 for (Object map : odimapping) {
            Mapping odimap =(Mapping)map ;
          //  if (odiscenario.getName() == "ETRM_ASX_PRICES_FILE_TO_LDS_MAP") {
                println(" Exporting mapping  "+odimap.getName());
                export.exportToXml(odimap, ExportFolderPath, OverWriteFile, RecursiveExport, EncdOption);
            //}
        }
		
		//Reading all procedures
		Collection odiproc= ((IOdiUserProcedureFinder) odiInstance.getTransactionalEntityManager().getFinder(OdiUserProcedure.class)).findAll();
		 for (Object proc : odiproc) {
            OdiUserProcedure odiprocedure =(OdiUserProcedure)proc ;
          //  if (odiscenario.getName() == "ETRM_ASX_PRICES_FILE_TO_LDS_MAP") {
                println(" Exporting procedure  "+odiprocedure.getName());
                export.exportToXml(odiprocedure, ExportFolderPath, OverWriteFile, RecursiveExport, EncdOption);
            //}
        }
        //Importing Scenarios in INSERT_UPDATE Mode
        // The below code import all the scenarios present in the Exported Folder path , so make sure older files are deleted
        //ImportServiceImpl importsrvc=new ImportServiceImpl(odiInstance_exec);
        //String[] XMLFiles=getXMLFiles(ExportFolderPath).split("n");
        //for (String xmlfile : XMLFiles) {
          //  System.out.println(" Importing scenario  XML "+xmlfile);
           // importsrvc.importObjectFromXml(importsrvc.IMPORT_MODE_SYNONYM_INSERT_UPDATE, xmlfile, true);
        //}
 
       // Commit and Close Transaction
       
       odiInstance.getTransactionManager().commit(trans);
       odiInstance.close();
 
 
    //Reading all the XML Files
   //public static String getXMLFiles(String DirectoryName){
   //
   //    String xmlfiles="";
   //    String files;
   //    File folder = new File(DirectoryName);
   //    File[] listOfFiles = folder.listFiles();
   //      for (int i = 0; i < listOfFiles.length; i++)
   //      {
   //       if (listOfFiles[i].isFile())
   //       {
   //       files = listOfFiles[i].getName();
   //           if (files.endsWith(".xml") || files.endsWith(".XML"))
   //           {
   //               xmlfiles+=DirectoryName+"/"+files+"n";
   //            }
   //       }}
   //    return xmlfiles;
  //}
 
