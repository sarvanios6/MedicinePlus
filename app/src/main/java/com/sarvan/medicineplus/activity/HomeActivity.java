package com.sarvan.medicineplus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sarvan.medicineplus.R;
import com.sarvan.medicineplus.fragment.AboutUsFragment;
import com.sarvan.medicineplus.fragment.ContactUsFragment;
import com.sarvan.medicineplus.fragment.HomeFragment;
import com.sarvan.medicineplus.fragment.NewsFragment;
import com.sarvan.medicineplus.fragment.SettingFragment;
import com.sarvan.medicineplus.others.Helper;
import com.sarvan.medicineplus.realm.Users;
import com.sarvan.medicineplus.realm.UsersRealm;

import io.realm.Realm;

public class HomeActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private RelativeLayout headerContainer;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;

    // tags used to attach the fragments
    public static final String MEDICINE_PLUS = "Medicine_plus";
    private static final String TAG = "HomeActivity";
    private static final String TAG_HOME = "home";
    private static final String TAG_NEWS = "news";
    private static final String TAG_SETTINGS = "settings";
    private static final String TAG_ABOUT_US = "about_us";
    private static final String TAG_CONTACT_US = "contact_us";
    public static String CURRENT_TAG = TAG_HOME;

    private static final String urlNavHeaderBg = "http://www.noormedical.com/files/credentialing.jpg";
    private static final String urlProfileImg = "https://cdn6.f-cdn.com/ppic/42449860/logo/10996896/wrUMb/profile_logo_.jpg";

    // index to identify current nav menu item
    public static int navItemIndex = 0;
    // Activity titles
    private String[] activityTitles;
    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    private ImageView imgNavHeaderBg;
    private FirebaseDatabase firebaseInstance;
    private DatabaseReference mFirebaseDatabaseRef;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private GoogleApiClient mGoogleApiClient;
    private Realm realm;
    private UsersRealm usersRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        usersRealm = new UsersRealm();
        mHandler = new Handler();

        // Initialize Firebase Auth and database
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationViewInit();
        // load nav menu header data
        loadNavHeader();
        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
        // FireBase Database
        firebaseInstance = FirebaseDatabase.getInstance();
        // store app title to 'app_title' node
        firebaseInstance.getReference(MEDICINE_PLUS);
        // Get reference to user node
        mFirebaseDatabaseRef = FirebaseDatabase.getInstance().getReference();
        if (mFirebaseUser != null) {
            Users users = new Users(mFirebaseUser.getDisplayName(), mFirebaseUser.getEmail(), mFirebaseUser.getPhotoUrl().toString(), mFirebaseUser.getUid());
            mFirebaseDatabaseRef.child(MEDICINE_PLUS).child("Users").child(mFirebaseUser.getDisplayName() + "_" + mFirebaseUser.getUid()).setValue(users);
            // update user list name
            if (mFirebaseUser.getDisplayName().equalsIgnoreCase("sarvan kumar")) {
                mFirebaseDatabaseRef.child(MEDICINE_PLUS).child("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Users users = postSnapshot.getValue(Users.class);
                            usersRealm.setName(users.getName());
                            usersRealm.setChannel(users.getChannel());
                            usersRealm.setEmail(users.getEmail());
                            usersRealm.setPhotoUrl(users.getphotoUrl());
                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(usersRealm);
                            realm.commitTransaction();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("HomeViewActivity", databaseError.getMessage());
                    }
                });
            }
        }
    }

    private void navigationViewInit() {
        // Navigation view Header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.user_name_tv);
        txtWebsite = (TextView) navHeader.findViewById(R.id.user_email_tv);
        headerContainer = (RelativeLayout) navHeader.findViewById(R.id.header_view_container);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        headerContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.stockInfoDialog(HomeActivity.this);
                drawer.closeDrawers();
            }
        });
        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        txtName.setText("MedicinePlus");
        String userEmail = "No Email";
        // check user availability
        if (mFirebaseUser != null) {
            userEmail = mFirebaseUser.getEmail();
        }
        txtWebsite.setText(userEmail);
        // loading header background image
        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);

        // Loading profile image
//        Glide.with(this).load(urlProfileImg)
//                .crossFade()
//                .thumbnail(0.5f)
//                .bitmapTransform(new CircleTransForm(this))
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(imgProfile);

        // showing dot next to notifications label
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
//            toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    /**
     * SelectNavigation Menu
     */
    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    /**
     * Set ToolBar Title based on navigation items.
     */
    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }


    /**
     * GetHomeFragment
     *
     * @return selected fragment
     */
    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home fragment
                return new HomeFragment();
            case 1:
                // news fragment
                return new NewsFragment();
            case 2:
                // settings fragment
                return new SettingFragment();
            case 3:
                // aboutUs fragment
                return new AboutUsFragment();
            case 4:
                // contactUs fragment
                return new ContactUsFragment();
            default:
                return new HomeFragment();
        }
    }

    /**
     * SetUpNavigationView
     */
    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_news:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_NEWS;
                        break;
                    case R.id.nav_settings:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_SETTINGS;
                        break;
                    case R.id.nav_about_us:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_ABOUT_US;
                        break;
                    case R.id.nav_contact_us:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_CONTACT_US;
                        break;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
                navigationViewInit();
                loadNavHeader();
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }
        super.onBackPressed();
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadNavHeader();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadNavHeader();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
}
