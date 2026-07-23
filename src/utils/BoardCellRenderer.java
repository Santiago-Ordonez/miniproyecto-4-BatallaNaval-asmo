package utils;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import model.Orientation;

public class BoardCellRenderer {

    private static final String IMAGE_PATH = "../resources/ship_";

    public static void updateCell(Pane cell, int value, boolean showShip, int part, int shipType, Orientation orientation) {
        if(cell.getChildren().isEmpty()){
            System.out.println("Error al obtener cell.children");
            return;
        }

        ImageView iv = (ImageView) cell.getChildren().get(0);
        iv.setImage(null);
        iv.setRotate(0);
        iv.setEffect(null);

        if(value == 0){
            cell.setStyle("-fx-border-color: black; -fx-background-color: lightblue");
            return;
        }

        if(value == 2){
            cell.setStyle("-fx-border-color: black; -fx-background-color: darkblue");
            return;
        }

        if (showShip || value == 4) {
            String filename = IMAGE_PATH + shipType + "/" + "seg_" + part + ".png";
            Image img = loadImage(filename);
            iv.setImage(img);
            if (orientation == Orientation.VERTICAL) {
                iv.setRotate(90);
            }
            if(value == 4){
                cell.setStyle("-fx-border-color: black; -fx-background-color: orange");
                applySunkEffect(iv);
            }
        }else{
            if(value == 1){
                cell.setStyle("-fx-border-color: black; -fx-background-color: lightblue");
                return;
            }else if(value == 3) {
                cell.setStyle("-fx-border-color: black; -fx-background-color: orange");
                return;
            }
        }

        if(value == 3){
            cell.setStyle("-fx-border-color: black; -fx-background-color: orange");
            applyHitEffect(iv);
        }
    }

    private static Image loadImage(String filename) {
        try {
            java.io.InputStream is = BoardCellRenderer.class.getResourceAsStream(filename);
            if (is == null) {
                System.out.println("No se encontró el recurso: " + filename);
                return null;
            }
            return new Image(is, 40, 40, true, true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void applyHitEffect(ImageView iv) {
        ColorAdjust adjust = new ColorAdjust();
        adjust.setHue(0.1);
        adjust.setBrightness(-0.2);
        adjust.setSaturation(0.8);
        iv.setEffect(adjust);
    }

    private static void applySunkEffect(ImageView iv) {
        ColorAdjust adjust = new ColorAdjust();
        adjust.setBrightness(-0.5);
        adjust.setSaturation(-0.3);
        iv.setEffect(adjust);
    }
}