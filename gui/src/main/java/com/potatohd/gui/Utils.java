package com.potatohd.gui; /**
 *
 * @author Michael Kandefer
 */
import java.io.File;

/* com.potatohd.Utils.java is used by FileChooserDemo2.java. */
public class Utils {
    public final static String snepslog = "snepslog";
    public final static String sneps = "sneps";
    public final static String xml = "xml";
    public final static String rdf = "rdf";
    public final static String owl = "owl";
    
    public final static String ras = "ras";
    public final static String xbm = "xbm";
    public final static String jpg = "jpg";
    public final static String pct = "pct";
    public final static String tga = "tga";
    public final static String psd = "psd";
    public final static String pcx = "pcx";
    public final static String bmp = "bmp";
    public final static String jpeg = "jpeg";
    public final static String png = "png";
    public final static String eps = "eps";
   
    /*
     * Get the extension of a file.
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
}