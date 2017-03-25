package com.example.adrian.avance;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;

import de.hdodenhof.circleimageview.CircleImageView;

public class Principal extends AppCompatActivity {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private DrawerLayout drawerLayout;

    private CircleImageView circleImageView;
    private TextView nombre_usuario, email_usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("InclusivApp");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        circleImageView =(CircleImageView) findViewById(R.id.profile_image);
        nombre_usuario = (TextView)findViewById(R.id.nom_usuario);
        email_usuario = (TextView)findViewById(R.id.correo_usuario);




        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);


        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);
        }


        setupNavigationDrawerContent(navigationView);



        if (AccessToken.getCurrentAccessToken() != null) {

            ProfileTracker profileTracker = new ProfileTracker(){

                @Override
                protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                    stopTracking();
                    Profile.setCurrentProfile(currentProfile);

                  //  Toast.makeText(getApplicationContext(),Profile.getCurrentProfile().toString(),Toast.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(),Profile.getCurrentProfile().getName(),Toast.LENGTH_LONG).show();

                    nombre_usuario.setText(Profile.getCurrentProfile().getName()+" "+Profile.getCurrentProfile().getLastName());
                    circleImageView.setImageURI(Profile.getCurrentProfile().getProfilePictureUri(200,200));
                }

            };

            profileTracker.startTracking();




        }




        setFragment(0);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void setupNavigationDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(

                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.menu_pagina_principal:
                                menuItem.setChecked(true);
                                setFragment(0);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.menu_cerrar_sesion:
                                menuItem.setChecked(true);

                                if (AccessToken.getCurrentAccessToken() == null) {
                                    menuItem.setTitle("Cerrar Sesión");
                                    goLoginScreen();

                                }else{

                                    LoginManager.getInstance().logOut();
                                    menuItem.setTitle("Iniciar Sesión");
                                }

                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;


                        }
                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setFragment(0);
        drawerLayout.closeDrawer(GravityCompat.START);
    }




    public void setFragment(int position) {
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        switch (position) {
            case 0:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                Mapa paginaPrincipalFragment = new Mapa();
                fragmentTransaction.replace(R.id.fragment, paginaPrincipalFragment);
                fragmentTransaction.commit();
                break;



        }
    }


    private void goLoginScreen() {
        Intent intent = new Intent(this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    public void logout(View view) {
        LoginManager.getInstance().logOut();
        goLoginScreen();
    }
}
