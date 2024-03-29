package jacied;


import com.jacied.IOLine;
import com.melloware.jintellitype.JIntellitype;
import com.melloware.jintellitype.JIntellitypeConstants;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ScreenShotUI extends Application {

    ImageView iv;
    Stage primary;
    IOLine io = new IOLine(new File("src/main/java/record.txt"));
    String path;
    File lastFile;

    @Override
    public void start(Stage primaryStage) {
        primary = primaryStage;
        makeShortcut();

        Button button = new Button("截图");
        Button keep = new Button("保存地址");
        Button openFile = new Button("打开所在文件夹");
        Button openPhoto = new Button("打开图片");
        CheckBox exit = new CheckBox("关闭时隐藏(ctrl+alt+shift+d关闭程序,ctrl+alt+shift+o打开程序界面)");
        CheckBox unShow = new CheckBox("打开时不显示界面");

        iv = new ImageView();
        iv.setFitWidth(850);
        iv.setPreserveRatio(true);

        TextField textFieldPath = new TextField();
        textFieldPath.setMinWidth(280.0);
        path = io.inPut(1);
        textFieldPath.setText(path);

        exit.setSelected(io.inPut(0).equals("1"));

        exit.setOnAction(event -> io.outPut(exit.isSelected() ? "1" : "0", 0));

        unShow.setSelected(io.inPut(2).equals("1"));

        unShow.setOnAction(event -> io.outPut(unShow.isSelected() ? "1" : "0", 2));

        VBox vBox = new VBox(20.0, new HBox(20.0, button, new Label("快捷键:ctrl+p或者F10"), new Label("保存地址:"), textFieldPath, keep),
                new HBox(20.0, openFile, openPhoto, exit,unShow),
                iv);

        Scene s = new Scene(vBox);

        button.setOnAction(event -> getImg());


        keep.setOnAction(event -> {
            if (textFieldPath.getText().equals("")) {
                io.outPut(path = "E:\\photo\\JavaScreenCapture", 1);
            } else {
                io.outPut(path = textFieldPath.getText(), 1);
            }
        });
        openFile.setOnAction(event -> {
            try {
                Runtime.getRuntime().exec("explorer.exe " + path);
            } catch (IOException e) {
            }
        });

        openPhoto.setOnAction(event -> {
            try {
                Runtime.getRuntime().exec("explorer.exe " + lastFile);
            } catch (IOException e) {
            }
        });

        primaryStage.setOnCloseRequest(event -> {
            if (!exit.isSelected()) {
                System.exit(0);
            } else {
                Platform.setImplicitExit(false);
            }
        });

        primaryStage.setHeight(600.0);
        primaryStage.setWidth(900.0);
        primaryStage.setScene(s);
        if (!unShow.isSelected()) primaryStage.show();
    }

    public void getImg() {
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e) {
        }
        Rectangle rec = new Rectangle(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        BufferedImage bufferedImage = robot.createScreenCapture(rec);
        WritableImage wi = SwingFXUtils.toFXImage(bufferedImage, null);

        iv.setImage(wi);

        String name = "Data" + System.currentTimeMillis() + ".png";
        lastFile = new File(path, name);
        try {
            ImageIO.write(bufferedImage, "png", lastFile);
        } catch (IOException e) {
        }

    }

    final int SCREEN_SHOT = 0;
    final int SYSTEM_EXIT = 1;
    final int SYSTEM_OPEN = 2;

    public void makeShortcut() {
        JIntellitype j = JIntellitype.getInstance();
        j.registerHotKey(SCREEN_SHOT, JIntellitypeConstants.MOD_CONTROL, 'P');
        j.registerHotKey(SCREEN_SHOT, 0, 121);
        j.registerHotKey(SYSTEM_EXIT, JIntellitypeConstants.MOD_CONTROL + JIntellitypeConstants.MOD_ALT + JIntellitypeConstants.MOD_SHIFT, 'D');
        j.registerHotKey(SYSTEM_OPEN, JIntellitypeConstants.MOD_CONTROL + JIntellitypeConstants.MOD_SHIFT + JIntellitypeConstants.MOD_ALT, 'O');
        j.addHotKeyListener(i -> {
            switch (i) {
                case SCREEN_SHOT -> getImg();
                case SYSTEM_EXIT -> System.exit(0);
                case SYSTEM_OPEN -> Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        primary.show();
                    }
                });
            }
        });
    }
}
