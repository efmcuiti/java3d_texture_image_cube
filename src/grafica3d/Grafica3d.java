package grafica3d;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.TextureCubeMap;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;

/**
 *
 * @author Emiliano
 */
public class Grafica3d implements Runnable {

    TransformGroup tra = new TransformGroup();
    Transform3D transformacion = new Transform3D();
    Thread sec = new Thread((Runnable) this);
    double y = 0;

    public Grafica3d() {
        BranchGroup grupo = new BranchGroup();
        Appearance appearance = new Appearance();
        Box cubo = new Box(
                0.4f, 0.4f, 0.4f, Box.GENERATE_TEXTURE_COORDS, appearance);
        buildCubeFaces(cubo);
        SimpleUniverse universo = new SimpleUniverse();

        sec.start();
        //permite reescribir las condiciones del cubo en tiempo de ejecución
        tra.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        tra.setTransform(transformacion);
        tra.addChild(cubo);
        grupo.addChild(tra);
        universo.getViewingPlatform().setNominalViewingTransform();
        universo.addBranchGraph(grupo);
    }

    public static void main(String[] args) {
        new Grafica3d();
    }
//metodo abstracto

    @Override
    public void run() {

        while (Thread.currentThread() == sec) {

            try {
                y = y + 0.01;
                transformacion.rotX(y);
                tra.setTransform(transformacion);
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(Grafica3d.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    /**
     * Establece una imagen por cada cara del cubo..
     */
    private void buildCubeFaces(Box cubo) {
        // I. Establecer la imagen por 'cara.
        ((Shape3D)cubo.getChild(TextureCubeMap.POSITIVE_X))
                .setAppearance(getImagefor(TextureCubeMap.POSITIVE_X));
        ((Shape3D)cubo.getChild(TextureCubeMap.NEGATIVE_X))
                .setAppearance(getImagefor(TextureCubeMap.NEGATIVE_X));
        ((Shape3D)cubo.getChild(TextureCubeMap.POSITIVE_Y))
                .setAppearance(getImagefor(TextureCubeMap.POSITIVE_Y));
        ((Shape3D)cubo.getChild(TextureCubeMap.NEGATIVE_Y))
                .setAppearance(getImagefor(TextureCubeMap.NEGATIVE_Y));
        ((Shape3D)cubo.getChild(TextureCubeMap.POSITIVE_Z))
                .setAppearance(getImagefor(TextureCubeMap.POSITIVE_Z));
        ((Shape3D)cubo.getChild(TextureCubeMap.NEGATIVE_Z))
                .setAppearance(getImagefor(TextureCubeMap.NEGATIVE_Z));
    }

    /**
     * Este método carga la imagen dependiendo de la cara del cubo.
     *
     * @param cara de aspecto del cubo.
     * @return La imagen para usar en la cara.
     */
    private Appearance getImagefor(int face) {
        Appearance app = new Appearance();
        BufferedImage bi;

        try {
            bi = ImageIO.read(new File(String.format("faceImg/face_%d.jpg", face)));
            TextureLoader loader = new TextureLoader(bi);
            app.setTexture(loader.getTexture());
            
            // Setting the capabilities for each texture in the face.
            TextureAttributes attributes = new TextureAttributes();
            attributes.setTextureMode(TextureAttributes.MODULATE);
            app.setTextureAttributes(attributes);
        } catch (IOException ex) {
            Logger.getLogger(Grafica3d.class.getName()).log(Level.SEVERE, null, ex);
        }
        return app;
    }
}
