package info.jmfavreau.bifrost;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


import info.jmfavreau.bifrost.color.FuzzyColorRules;
import info.jmfavreau.bifrost.color.SemanticColorRules;

import static info.jmfavreau.bifrost.R.layout.activity_bifrost;


public class Bifrost extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FuzzyColorRules.load(this);
        SemanticColorRules.loadDefault(this);
        setContentView(R.layout.activity_bifrost);
    }

}
