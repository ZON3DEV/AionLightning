package org.openaion.loginserver;

import org.apache.log4j.Logger;
import org.openaion.commons.database.DatabaseFactory;
import org.openaion.commons.database.dao.DAOManager;
import org.openaion.commons.services.LoggingService;
import org.openaion.commons.utils.AEInfos;
import org.openaion.commons.utils.ExitCode;
import org.openaion.loginserver.configs.Config;
import org.openaion.loginserver.controller.BannedIpController;
import org.openaion.loginserver.network.IOServer;
import org.openaion.loginserver.network.ncrypt.KeyGen;
import org.openaion.loginserver.utils.DeadLockDetector;
import org.openaion.loginserver.utils.ThreadPoolManager;
import org.openaion.loginserver.utils.Util;


/**
 * @author -Nemesiss-
 */
public class LoginServer
{
    /**
     * Logger for this class.
     */
    private static final Logger	log = Logger.getLogger(LoginServer.class);

    /**
     * @param args
     */
    public static void main(String[] args)
    {
    	long start = System.currentTimeMillis();
    	
        LoggingService.init();

		Config.load();

		Util.printSection("DataBase");
		DatabaseFactory.init("./config/network/database.properties");
		DAOManager.init();

        /** Start deadlock detector that will restart server if deadlock happened */
        new DeadLockDetector(60, DeadLockDetector.RESTART).start();
        ThreadPoolManager.getInstance();


        /**
         * Initialize Key Generator
         */
        try
        {
        	Util.printSection("KeyGen");
            KeyGen.init();
        }
        catch (Exception e)
        {
            log.fatal("Failed initializing Key Generator. Reason: " + e.getMessage(), e);
            System.exit(ExitCode.CODE_ERROR);
        }

        Util.printSection("GSTable");
        GameServerTable.load();
        Util.printSection("BannedIP");
        BannedIpController.load();

        // DONE! flood protector
        // TODO! brute force protector

        Util.printSection("IOServer");
        IOServer.getInstance().connect();
        Runtime.getRuntime().addShutdownHook(Shutdown.getInstance());

        Util.printSection("System");
        AEInfos.printAllInfos();
        
        Util.printSection("LoginServerLog");
        log.info("Login Server started in " + (System.currentTimeMillis() - start) / 1000 + " seconds.");
    }
}
