package com.example.zerowastehero.Main;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.zerowastehero.DataBinding.Model.PostModel;
import com.example.zerowastehero.DataBinding.Model.ReplyModel;
import com.example.zerowastehero.DataBinding.Model.UserModel;
import com.example.zerowastehero.DataBinding.ViewModel.SharedPostModel;
import com.example.zerowastehero.DataBinding.ViewModel.SharedReplyModel;
import com.example.zerowastehero.DataBinding.ViewModel.SharedUserModel;
import com.example.zerowastehero.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<UserModel> userModels = new ArrayList<>();
    private ArrayList<PostModel> postModels = new ArrayList<>();
    private ArrayList<ReplyModel> replyModels = new ArrayList<>();
    private SharedPostModel sharedPostModel;
    private SharedUserModel sharedUserModel;
    private SharedReplyModel sharedReplyModel;

    private BottomNavigationView bottomNavigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.TBMain);
        setSupportActionBar(toolbar);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.NHFMain);
        NavController navController = navHostFragment.getNavController();

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        setupBottomNavMenu(navController);

        setupUserModels();
        setupPostModels();
        setupReplyModels();
        sharedUserModel = new ViewModelProvider(this).get(SharedUserModel.class);
        sharedPostModel = new ViewModelProvider(this).get(SharedPostModel.class);
        sharedReplyModel = new ViewModelProvider(this).get(SharedReplyModel.class);
        sharedUserModel.setUsers(userModels);
        sharedPostModel.setPosts(postModels);
        sharedReplyModel.setReplies(replyModels);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.DLMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void setupUserModels() {
        userModels.add(new UserModel("EcoWarrior", "Passionate about zero waste living", "ecowarrior@gmail.com"));
        userModels.add(new UserModel("RecycleQueen", "Lover of all things recyclable", "recyclequeen@gmail.com"));
    }

    private void setupPostModels() {
        postModels.add(new PostModel("Today’s Achievement!","Cleaned up the park near my neighborhood! It was so satisfying to see it spotless again. Let’s keep our environment clean!", userModels.get(0).getUserID(), userModels.get(0).getUsername()));
        postModels.add(new PostModel("DIY Tips for Plastic Bottles!","Turn your old soda bottles into hanging garden pots! Here’s a quick guide: Cut, paint, and hang. Let’s upcycle instead of waste.", userModels.get(1).getUserID(), userModels.get(1).getUsername()));
        postModels.add(new PostModel("Rubbish Spotted in Town","Spotted litter near the riverbank. Let’s team up this weekend to clean it up! Anyone nearby, join me?", userModels.get(0).getUserID(), userModels.get(0).getUsername()));
        postModels.add(new PostModel("Community Recycling Effort!","A big thank you to everyone who participated in today’s recycling drive. Together, we collected over 200kg of waste!", userModels.get(1).getUserID(), userModels.get(1).getUsername()));
        postModels.add(new PostModel("Beach Clean-up Success!","Spent the morning cleaning up the beach with friends. The turtles will thank us! Let’s keep this energy going.", userModels.get(0).getUserID(), userModels.get(0).getUsername()));
        postModels.add(new PostModel("Upcycling Inspiration!","Made an eco-friendly tote bag from old T-shirts. Stylish and sustainable! Who wants to learn how?", userModels.get(1).getUserID(), userModels.get(1).getUsername()));
        postModels.add(new PostModel("Urban Garden Update","The community garden is thriving thanks to compost made from kitchen scraps. Nature wins!", userModels.get(0).getUserID(), userModels.get(0).getUsername()));
        postModels.add(new PostModel("Litter Alert!","Noticed trash piling up at the old playground. Let’s organize a clean-up crew. Volunteers needed!", userModels.get(1).getUserID(), userModels.get(1).getUsername()));
    }

    private void setupReplyModels() {
        replyModels.add(new ReplyModel("Great job! Keep it up.", postModels.get(0).getPostID(), userModels.get(0).getUserID(), userModels.get(0).getUsername()));
        replyModels.add(new ReplyModel("Amazing effort! You’re an inspiration.", postModels.get(1).getPostID(), userModels.get(1).getUserID(), userModels.get(1).getUsername()));
        replyModels.add(new ReplyModel("Let’s join hands to clean up together!", postModels.get(2).getPostID(), userModels.get(0).getUserID(), userModels.get(0).getUsername()));
        replyModels.add(new ReplyModel("Wow, that DIY tip is super creative. Thanks!", postModels.get(3).getPostID(), userModels.get(1).getUserID(), userModels.get(1).getUsername()));
        replyModels.add(new ReplyModel("I’ll bring my friends for the clean-up this weekend.", postModels.get(4).getPostID(), userModels.get(1).getUserID(), userModels.get(0).getUsername()));
        replyModels.add(new ReplyModel("Such a thoughtful initiative. Kudos to you!", postModels.get(5).getPostID(), userModels.get(0).getUserID(), userModels.get(1).getUsername()));
        replyModels.add(new ReplyModel("Turtles are lucky to have you around!", postModels.get(6).getPostID(), userModels.get(1).getUserID(), userModels.get(0).getUsername()));
        replyModels.add(new ReplyModel("Eco-friendly goals! Love the tote bag idea.", postModels.get(7).getPostID(), userModels.get(0).getUserID(), userModels.get(1).getUsername()));
        replyModels.add(new ReplyModel("Count me in for the playground clean-up.", postModels.get(0).getPostID(), userModels.get(1).getUserID(), userModels.get(0).getUsername()));
        replyModels.add(new ReplyModel("Amazing teamwork! Keep the environment clean.", postModels.get(1).getPostID(), userModels.get(0).getUserID(), userModels.get(1).getUsername()));
    }

    public void setupBottomNavMenu(NavController navController) {
        BottomNavigationView bottomNavigationView =findViewById(R.id.BottomNavView);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.NHFMain).navigateUp();
    }
}