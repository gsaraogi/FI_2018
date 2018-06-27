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


public  OdiScenario export_scen (obj_name)
{
       ExportServiceImpl export=new ExportServiceImpl(odiInstance);
       EncodingOptions EncdOption = new EncodingOptions();
        //Reading all scenario
        Collection odiscen= ((IOdiScenarioFinder) odiInstance.getTransactionalEntityManager().getFinder(OdiScenario.class)).findAll();
        for (Object scen : odiscen) 
        {
            OdiScenario odiscenario =(OdiScenario)scen ;
              if (odiscenario.getName() == obj_name) 
              {
                println(" Exporting scenario  "+odiscenario.getName());
                return odiscenario;     
              }
        }
}

public OdiPackage export_pack (obj_name)
{
       ExportServiceImpl export=new ExportServiceImpl(odiInstance);
       EncodingOptions EncdOption = new EncodingOptions();
 //Reading all packages
       Collection odipkg= ((IOdiPackageFinder) odiInstance.getTransactionalEntityManager().getFinder(OdiPackage.class)).findAll();
       for (Object pkg : odipkg) 
       {
            OdiPackage odipackage =(OdiPackage)pkg ;
            if (odipackage.getName() == obj_name) 
            {
                println(" Exporting package  "+odipackage.getName());
                return odipackage;    
            }
        }
}

public Mapping export_map (obj_name)
{
       ExportServiceImpl export=new ExportServiceImpl(odiInstance);
       EncodingOptions EncdOption = new EncodingOptions();
       //Reading all mappings
       Collection odimapping= ((IMappingFinder) odiInstance.getTransactionalEntityManager().getFinder(Mapping.class)).findAll();
       for (Object map : odimapping) 
       {
            Mapping odimap =(Mapping)map ;
            if (odimap.getName() == obj_name) 
            {
                println(" Exporting mapping  "+odimap.getName());
                return odimap;    
            }
        }
}

public OdiUserProcedure export_proc (obj_name)
{
       ExportServiceImpl export=new ExportServiceImpl(odiInstance);
       EncodingOptions EncdOption = new EncodingOptions();
       //Reading all procedures
       Collection odiproc= ((IOdiUserProcedureFinder) odiInstance.getTransactionalEntityManager().getFinder(OdiUserProcedure.class)).findAll();
       for (Object proc : odiproc) 
       {
            OdiUserProcedure odiprocedure =(OdiUserProcedure)proc ;
            if (odiprocedure.getName() == obj_name) 
            {
                println(" Exporting procedure  "+odiprocedure.getName());
                return odiprocedure;    
            }
        }
}

public static void main (String[] args) throws IOException
{	
Properties props = new Properties();
FileInputStream fs = new FileInputStream("C:\\Users\\gsaraogi\\Desktop\\db.properties");
props.load(fs);
fs.close();


String url = props.getProperty("Url");
String driver = props.getProperty("Driver");
String user = props.getProperty("Master_User");
String password = props.getProperty("Master_Pass");
String rep = props.getProperty("WorkRep");
String odi_user = props.getProperty("Odi_User");
String odi_pass = props.getProperty("Odi_Pass");
String ExportFolderPath=props.getProperty("Export_Path");

String csvFile = "C:\\Users\\gsaraogi\\Desktop\\sample.csv";
BufferedReader br = new BufferedReader(new FileReader(csvFile));
String line = "";
String cvsSplitBy = ",";
 
Boolean RecursiveExport        = true;
Boolean OverWriteFile          = true;


MasterRepositoryDbInfo masterInfo = new MasterRepositoryDbInfo(url, driver, user,password.toCharArray(), new PoolingAttributes());
 
/// Development Repository
WorkRepositoryDbInfo workInfo = new WorkRepositoryDbInfo(rep, new PoolingAttributes());
OdiInstance odiInstance=OdiInstance.createInstance(new OdiInstanceConfig(masterInfo,workInfo));
Authentication auth = odiInstance.getSecurityManager().createAuthentication(odi_user,odi_pass.toCharArray());
odiInstance.getSecurityManager().setCurrentThreadAuthentication(auth);
ITransactionStatus trans = odiInstance.getTransactionManager().getTransaction(new DefaultTransactionDefinition());
 
ExportServiceImpl export=new ExportServiceImpl(odiInstance);
EncodingOptions EncdOption = new EncodingOptions();
  
println("odi repository connection successful");      
while ((line = br.readLine()) != null) 
{
        String[] col = line.split(cvsSplitBy);
        obj_type = col[0]
        obj_name = col[1]
		if ( obj_type == 'scenario' )
		{
		odiscenario=export_scen(obj_name);
                export.exportToXml(odiscenario, ExportFolderPath, OverWriteFile, RecursiveExport, EncdOption);
		}
		
		if ( obj_type == 'package' )
		{
		odipackage=export_pack(obj_name);
                export.exportToXml(odipackage, ExportFolderPath, OverWriteFile, RecursiveExport, EncdOption);
		}
		
		if ( obj_type == 'mapping' )
		{
		odimap=export_map(obj_name);
		export.exportToXml(odimap, ExportFolderPath, OverWriteFile, RecursiveExport, EncdOption);
                }
		
		if ( obj_type == 'procedure' )
		{
		odiprocedure=export_proc(obj_name);
                export.exportToXml(odiprocedure, ExportFolderPath, OverWriteFile, RecursiveExport, EncdOption);
		}


}
    odiInstance.getTransactionManager().commit(trans);
    odiInstance.close();
}

 
