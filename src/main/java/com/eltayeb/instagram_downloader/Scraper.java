package com.eltayeb.instagram_downloader;

import com.eltayeb.instagram_downloader.utils.Helpers;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

import java.nio.file.StandardCopyOption;
import java.time.Duration;


public class Scraper {
    final static String USER_AGENT = "Mozilla";
    public static void main(String[] args) {

        String targetDirectory = "/Users/asdf/Desktop/scrapping";
        while(true) {
            String url = JOptionPane.showInputDialog("Enter The Url :");
            downloadImage(url, targetDirectory);
        }


    }


    private static void downloadImage(String url, String targetDirectory){
        try{
            System.out.println("===========================");
            System.out.println("Loading ...");
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.addArguments("--headless");
            firefoxOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
            WebDriver driver = new FirefoxDriver(firefoxOptions);
            driver.get(url);
            Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("_aagv")));

            WebElement imgMainDiv = driver.findElement(By.className("_aagu"));
//            System.out.println(imgMainDiv.getTagName());
            WebElement img = imgMainDiv.findElement(By.cssSelector("._aagv img"));
            String imageUrl = img.getAttribute("src");
//            System.out.println("image url : " + imageUrl);
//            String targetDirectory = "/Users/asdf/Desktop/scrapping";

            String[] tempName = imageUrl.split("/");
            String filename = tempName[tempName.length-1].split("[?]")[0];
            InputStream inputStream = URI.create(imageUrl).toURL().openStream();
            HttpURLConnection conn = (HttpURLConnection)URI.create(imageUrl).toURL().openConnection();

            Path targetPath = new File(targetDirectory + File.separator + filename + ".jpg").toPath();
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);

            int BYTES_PER_KB = 1024;
            double fileSize = ((double)conn.getContentLength() / BYTES_PER_KB);

            System.out.println("Media file downloaded successfully.");
            System.out.println(String.format("Media Location: %s", targetPath));
            System.out.println(String.format("Media Name: %s", filename));
            System.out.println(String.format("Media Size: %.2f kb", fileSize));
            System.out.println(String.format("Media Type: %s", Helpers.mediaType(filename)));
            driver.close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void downloadVideo(String url, String targetDirectory){
        try {
            WebDriver driver = new FirefoxDriver();
            driver.get(url);
            Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(60));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("x1i10hfl")));
            WebElement video = driver.findElement(By.cssSelector(".x5yr21d .x1uhb9sk .xh8yej3"));
            System.out.println(video.getTagName());
            System.out.println(video.getAttribute("src"));
            String videoWithBlobUrl = video.getAttribute("src");
            String videoUrl = videoWithBlobUrl.split(":")[1] + ":" +videoWithBlobUrl.split(":")[2];

            System.out.println(String.format("Video url: %s", videoUrl));

            String urlArray[] = videoUrl.split("/");

            InputStream in = URI.create(videoUrl).toURL().openStream();
            HttpURLConnection conn = (HttpURLConnection)URI.create(videoUrl).toURL().openConnection();
            String filename = urlArray[3] + ".mp4";
            Path targetPath = new File(targetDirectory + File.separator + filename ).toPath();
            Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);

            int BYTES_PER_KB = 1024;
            double fileSize = ((double)conn.getContentLength() / BYTES_PER_KB);

            System.out.println("Media file downloaded successfully.");
            System.out.println(String.format("Media Location: %s", targetPath));
            System.out.println(String.format("Media Name: %s", filename));
            System.out.println(String.format("Media Size: %.2f kb", fileSize));
            System.out.println(String.format("Media Type: %s", Helpers.mediaType(filename)));

        } catch(Exception e) {
            e.printStackTrace();
        }

    }


}
