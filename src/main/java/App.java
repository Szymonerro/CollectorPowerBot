import enums.CardPack;
import model.TokenStatus;
import service.AuthService;
import service.PackService;
import service.TokenService;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class App {
    private static final Logger logger = Logger.getLogger(App.class.getName());

    public static void main(String[] args) throws Exception {
        if (logger.getHandlers().length == 0) {
            FileHandler fileHandler = new FileHandler("tokenfarm.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        }

        AuthService auth = new AuthService("http://46.62.175.196");
        auth.login("", ""); //provide your login and password
        logger.info("Logged in successfully.");

        TokenService tokenService = new TokenService(auth, logger);
        PackService packService = new PackService(auth, logger);

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {
            try {
                TokenStatus status = tokenService.checkStatus();
                logger.info("Actual token status: " + status.getCurrentTokens() + "/" + status.getMaxTokens());

                logger.info("Starting burst token collection: 3 parallel requests");
                tokenService.collectTokensParallel(3);

                status = tokenService.checkStatus();
                logger.info("Status after burst: " + status.getCurrentTokens() + "/" + status.getMaxTokens() +
                        " | Total collected: " + status.getTotalCollected());

                if (status.getCurrentTokens() >= 20) {
                    logger.info("Token count >= 20, buying 40 packs in parallel");
                    packService.buyPackParallel(CardPack.EGYPT_OF_DESIRE, 40);
                    status = tokenService.checkStatus();
                    logger.info("Token status after shopping: " + status.getCurrentTokens() + "/" + status.getMaxTokens());
                }

            } catch (Exception e) {
                logger.warning("Scheduled task error: " + e.getMessage());
            }
        }, 0, 60500, TimeUnit.MILLISECONDS);
    }
}