/*
 * Bifröst - A camera-to-speech system for Android
 * Copyright (C) 2015 Jean-Marie Favreau <jeanmarie.favreau@free.fr>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package info.jmfavreau.bifrostcore.color;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.jmfavreau.bifrostcore.R;


/**
 * Created by Jean-Marie Favreau on 05/04/15.
 */
public class SemanticColorRules {

    private static Activity activity = null;
    private static SemanticColorRules instance = null;
    private static List<String> namespaces = null;
    private static List<String> namespacesIDs = null;

    private String namespace;
    private Map<String, String> translations = null;
    private List<SemanticRule> rules = null;
    private String preprocessing = null;

    private SemanticColorRules(Activity activity, String nspace) throws Exception {
        loadRules(activity, nspace);
    }

    private void loadRules(Activity activity, String nspace) throws Exception {
        String idNspace = getIDFromNameSpace(nspace);
        if (idNspace.equals(namespace))
            return;

        translations = new HashMap<>();
        rules = new ArrayList<>();

        Resources res = activity.getResources();

        String name = idNspace + "_color_names";
        int xmlID = res.getIdentifier(name, "xml", activity.getPackageName());
        if (xmlID == 0)
            throw new FileNotFoundException();
        XmlResourceParser parser = res.getXml(xmlID);
        if (parser == null)
            throw new FileNotFoundException();
        parser.next();
        int eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if(eventType == XmlPullParser.START_TAG) {
                if(parser.getName().equals("color-names")) {
                    String preprocess = parser.getAttributeValue(null, "preprocessing");
                    if (preprocess != null)
                        preprocessing = preprocess;
                    String ns = parser.getAttributeValue(null, "id");
                    if (ns != null)
                        namespace = ns;
                }
                else if (parser.getName().equals("translation")) {
                    String english = parser.getAttributeValue(null, "en");
                    String translation = parser.getAttributeValue(null, "tr");
                    if (english != null && translation != null)
                        translations.put(english, translation);
                }
                else if (parser.getName().equals("rule")) {
                    SemanticRule sr = SemanticRule.parse(parser);
                    if (sr != null)
                        rules.add(sr);
                }
            }
            eventType = parser.next();
        }

    }

    public static void load(Activity act, String nspace) {
        if (instance == null) {
            try {
                activity = act;
                loadNamespaces();
                if (nspace.compareTo("") == 0)
                    instance = new SemanticColorRules(activity, getDefaultNameSpace());
                else
                    instance = new SemanticColorRules(activity, nspace);
            }
            catch (IOException e){
                Log.e("SemanticColorRules", "IO exception");
            }
            catch (XmlPullParserException e) {
                Log.e("SemanticColorRules", "XML pull parser exception");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
        }
        else {
            try {
                instance.loadRules(act, nspace);
            }
            catch (IOException e){
                Log.e("SemanticColorRules", "IO exception");
            }
            catch (XmlPullParserException e) {
                Log.e("SemanticColorRules", "XML pull parser exception");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
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




    public static String translate(String value) {
        return instance.translations.get(value);
    }

    public void setSemanticFromFuzzy(SemanticColor semanticColor, FuzzyColor color) {
        FuzzyColor realColor = color;
        if (preprocessing != null) {
            if (preprocessing.equals("defuzzification-unary")) {
                realColor = color.getDefuzzificationUnary();
            }
        }
        for(SemanticRule r: rules) {
            if (r.accept(realColor)) {
                r.toSemanticColor(semanticColor, realColor);
                break;
            }
        }
        semanticColor = SemanticColor.unknownColor();

    }
}
