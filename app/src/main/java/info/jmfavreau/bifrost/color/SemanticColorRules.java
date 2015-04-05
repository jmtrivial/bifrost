package info.jmfavreau.bifrost.color;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import info.jmfavreau.bifrost.R;


/**
 * Created by Jean-Marie Favreau on 05/04/15.
 */
public class SemanticColorRules {

    private static Activity activity = null;
    private static SemanticColorRules instance = null;
    private static List<String> namespaces = null;
    private static List<String> namespacesIDs = null;
    private String namespace;

    private SemanticColorRules(Activity activity, String nspace) throws XmlPullParserException, IOException {
        loadRules(activity, nspace);
    }

    private void loadRules(Activity activity, String nspace) {
        // TODO
    }

    public static void load(Activity act, String nspace) {
        if (instance == null) {
            try {
                activity = act;
                loadNamespaces();
                if (nspace.compareTo("") == 0)
                    instance = new SemanticColorRules(activity, getDefaultIDNameSpace());
                else
                    instance = new SemanticColorRules(activity, nspace);
            }
            catch (IOException e){
                Log.e("SemanticColorRules", "IO exception");
            }
            catch (XmlPullParserException e) {
                Log.e("SemanticColorRules", "XML pull parser exception");
            }
            finally {
            }
        }
    }

    public static void loadDefault(Activity activity) {
        load(activity, "");
    }

    public static SemanticColorRules getInstance() throws Resources.NotFoundException {
        if (instance == null) {
            throw new Resources.NotFoundException();
        }
        else
            return instance;
    }

    static public List<String> getExistingNameSpaces() throws IOException, XmlPullParserException {
        if (namespaces == null) {
            loadNamespaces();
        }
        return namespaces;
    }

    static private String getIDFromNameSpace(String ns) {
        assert(namespaces.size() == namespacesIDs.size());
        for(int i = 0; i != namespaces.size(); ++i)
            if (ns.compareTo(namespaces.get(i)) == 0)
                return namespacesIDs.get(i);
        return "";
    }

    static private String getDefaultNameSpace() {
        assert(namespaces != null);
        assert(!namespaces.isEmpty());
        return namespaces.get(0);
    }

    static private String getDefaultIDNameSpace() {
        assert(namespacesIDs != null);
        assert(!namespacesIDs.isEmpty());
        return namespacesIDs.get(0);
    }


    private static void loadNamespaces() throws IOException, XmlPullParserException {
        namespaces = new ArrayList<>();
        namespacesIDs = new ArrayList<>();
        Resources res = activity.getResources();
        XmlResourceParser parser = res.getXml(R.xml.color_namespaces);
        if (parser == null)
            throw new FileNotFoundException();
        parser.next();
        int eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if(eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("namespace")) {
                    Boolean default_ns = parser.getAttributeBooleanValue(null, "default", false);
                    String nsID = parser.getAttributeValue(null, "id");
                    String ns = parser.getAttributeValue(null, "name");
                    if (default_ns != null && default_ns) {
                        namespaces.add(0, ns);
                        namespacesIDs.add(0, nsID);
                    }
                    else  {
                        namespaces.add(ns);
                        namespacesIDs.add(nsID);
                    }
                }
            }
            eventType = parser.next();
        }


    }
}
