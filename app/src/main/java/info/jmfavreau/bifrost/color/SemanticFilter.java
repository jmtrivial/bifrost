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

package info.jmfavreau.bifrost.color;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jean-Marie Favreau on 05/04/15.
 */
public class SemanticFilter {
    private static String equivalent = "~";
    private static String or = ",";
    private static enum Operator {
        equiv_operator, or_operator, final_node
    };


    private List<SemanticFilter> subfilters = null;
    private Operator kind;
    private String node = null;


    public SemanticFilter(String s) throws Exception {
        if (s.contains("(") || s.contains(")"))
            throw new Exception();
        if (s.contains(or)) {
            String[] sub = s.split(or);
            kind = Operator.or_operator;
            subfilters = new ArrayList<SemanticFilter>();
            for(int i = 0; i != sub.length; ++i)
                subfilters.add(new SemanticFilter(sub[i]));
        }
        else if (s.contains(equivalent)) {
            String[] sub = s.split(equivalent);
            kind = Operator.equiv_operator;
            subfilters = new ArrayList<SemanticFilter>();
            for(int i = 0; i != sub.length; ++i)
                subfilters.add(new SemanticFilter(sub[i]));
        }
        else {
            kind = Operator.final_node;
            node = s;
        }


    }

    private double getFuzzyValue(List<FuzzyColorElement> components) {
        if (kind != Operator.final_node)
            return -1.;
        for(FuzzyColorElement e: components)
            if (e.getName().equals(node))
                return e.getValue();
        return -1.;
    }

    public boolean match(List<FuzzyColorElement> components) {
        if (kind == Operator.final_node) {
            if (node.equals("*")) {
                return true;
            }
            FuzzyColorElement cc = FuzzyColorElement.getMainComponent(components);
            if (cc.getName().equals(node)) {
                return true;
            }
            return false;
        }
        else if (kind == Operator.or_operator) {
            assert(subfilters != null);
            for (SemanticFilter f : subfilters)
                if (f.match(components)) {
                    return true;
                }
            return false;
        }
        else if (kind == Operator.equiv_operator) {
            List<Double> values = new ArrayList<>();
            // build the list of the fuzzy values corresponding to each
            // subpart of the filter
            for(SemanticFilter f: subfilters) {
                double r = f.getFuzzyValue(components);
                if (r < 0)
                    return false;
                values.add(f.getFuzzyValue(components));
            }
            if (values.size() != subfilters.size())
                return false;

            // compute the mean
            double mean = 0;
            for(Double v: values)
                mean += v;
            mean /= values.size();
            // check if these values are equivalent

            for(Double v: values)
                if (Math.abs(v - mean) > 0.25)
                    return false;
            return true;
        }
        return false;
    }
}
