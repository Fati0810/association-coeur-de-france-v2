package com.association_coeur_de_france.controller;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.association_coeur_de_france.R;
import com.association_coeur_de_france.controller.fragments.FooterFragment;
import com.association_coeur_de_france.controller.fragments.HomeFragment;
import com.association_coeur_de_france.controller.fragments.DonFragment;
import com.association_coeur_de_france.controller.fragments.MessageFragment;
import com.association_coeur_de_france.controller.fragments.MouvementFragment;
import com.association_coeur_de_france.controller.fragments.ProfilFragment;

public class MainActivity extends AppCompatActivity implements FooterFragment.OnFooterButtonClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Charger le fragment Home par défaut
        loadFragment(new HomeFragment());
    }

    @Override
    public void onFooterButtonClick(int buttonId) {
        Fragment fragment = null;

        if (buttonId == R.id.btn_accueil) {
            fragment = new HomeFragment();
        } else if (buttonId == R.id.btn_adhesion) {
            fragment = new DonFragment();
        } else if (buttonId == R.id.btn_message) {
            fragment = new MessageFragment();
        } else if (buttonId == R.id.btn_profil) {
            fragment = new ProfilFragment();
        } else if (buttonId == R.id.btn_mouvement) {
            fragment = new MouvementFragment();
        }

        if (fragment != null) {
            loadFragment(fragment);
        }
    }


    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainContent, fragment)
                .commit();
    }
}
