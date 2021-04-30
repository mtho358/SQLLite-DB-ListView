package com.coolcats.sqlitedatabaselistview.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import com.coolcats.sqlitedatabaselistview.R;
import com.coolcats.sqlitedatabaselistview.databinding.ActivityMainBinding;
import com.coolcats.sqlitedatabaselistview.model.User;
import com.coolcats.sqlitedatabaselistview.model.db.UserDatabaseHelper;
import com.coolcats.sqlitedatabaselistview.util.Logger;
import com.coolcats.sqlitedatabaselistview.util.Position;
import com.coolcats.sqlitedatabaselistview.view.adapter.UserAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.coolcats.sqlitedatabaselistview.model.db.UserDatabaseHelper.ID_COLUMN;
import static com.coolcats.sqlitedatabaselistview.model.db.UserDatabaseHelper.NAME_COLUMN;
import static com.coolcats.sqlitedatabaselistview.model.db.UserDatabaseHelper.POSITION_COLUMN;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

/*
        When a new user opens the application the first time, there is normally a tutorial
        about how to use the application. For example, when you install a new game and open it
        for the first time you normally have a setup screen followed by a small tutorial that
        break downs the different buttons and what they do.

        The following gives an example of how to set that up.
            - Start with creating a reference to the SharedPreference class
            - Next you will need to initalize the reference
            - Then create a way to determine if this is the first time the application has been opened
            - Then show a welcome dialog
     */

    Logger log = new Logger();
    private SharedPreferences sharedPreferences;
    //DatabaseHelper
    private UserDatabaseHelper dbHelper;
    //This will bind the views
    private ActivityMainBinding binding;
    private final List<String> options = new ArrayList<String>(
            Arrays.asList(
                    "ANDROID_DEVELOPER",
                    "IOS_DEVELOPER",
                    "MANAGER"
            )
    );
    //ID or Index value of the position selected from the spinner
    private int spinnerId = 0;
    //UserAdapter
    private final UserAdapter userAdapter = new UserAdapter(new ArrayList<>());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        if (sharedPreferences.getBoolean("FIRST_TIME", true)) {
            sharedPreferences.edit().putBoolean("FIRST_TIME", false);
            showWelcomeDialog();
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item_layout, R.id.item_textview, options);

        //binding of the ListView
        binding.userDetailsListview.setAdapter(userAdapter);

        //binding of the spinner
        binding.positionSpinner.setAdapter(spinnerAdapter);
        binding.positionSpinner.setOnItemSelectedListener(this);
        Logger.logMessage("Spinner: " + Position.valueOf(options.get(spinnerId)) + " selected");
        dbHelper = new UserDatabaseHelper(this);


        //binding of the addUser button
        binding.addUserButton.setOnClickListener(view -> {
            if (checkInput()) {
                User newUser = new User(binding.editNameEdittext.getText().toString().trim(),
                        Position.valueOf(options.get(spinnerId)));
                Logger.logMessage("User " + newUser.getName() + " created");
                Logger.logMessage("Spinner: " + Position.valueOf(options.get(spinnerId)) + " selected");
                dbHelper.insertUser(newUser);
                Logger.logMessage("New user inserted into database");
                readDB();
                Logger.logMessage("Updated version of database");
                binding.editNameEdittext.setText("");
            }
        });

        binding.userDetailsListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, UserDetailsActivity.class);
                log.logMessage("Item: " + position + "was selected");
                Bundle bundle = new Bundle();
                bundle.putSerializable("READ", userAdapter.getItem(position));
                log.logMessage("Bundle created with" + userAdapter.getItemId(position));
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
    }

    private boolean checkInput() {

        if (binding.editNameEdittext.getText().toString().isEmpty()) {
            toastMessage("Name cannot be empty");
            return false;
        } else if (binding.editNameEdittext.getText().toString().length() < 4) {
            toastMessage("Name cannot be less than 3 characters in length.");
            return false;
        }

        return true;
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void readDB() {
        Cursor dbC = dbHelper.getAllUsers();
        dbC.moveToPosition(-1);
        List<User> users = new ArrayList<>();

        StringBuilder readString = new StringBuilder();

        while (dbC.moveToNext()) {
            int id = dbC.getInt(dbC.getColumnIndex(ID_COLUMN));
            String name = dbC.getString(dbC.getColumnIndex(NAME_COLUMN));
            String positionName = dbC.getString(dbC.getColumnIndex(POSITION_COLUMN));

            User user = new User(name, id, Position.valueOf(positionName));
            users.add(user);

            readString.append(user.getName()).append(" : ").append(user.getPosition().name()).append("\n");
        }

        dbC.close();
        displayUsers(users);
    }

    private void displayUsers(List<User> users) {
        userAdapter.upDateList(users);
        Logger.logMessage("Users list updated");
    }


    /*
        The showWelcomeDialog will display the "Tutorial" for the first time users.
        This method makes use of the AlertDialog and the Toast classes.

        The .edit() will allow you to make a change to the sharedPreference - which
        we will use to change the value for FIRST_TIME to false to prevent the "tutorial"
        from appearing each time the user opens the application.

        You can also use the dialogInterface.dismiss(); to allow the user to close
        the AlertDialog without interacting with the buttons.
     */
    private void showWelcomeDialog() {
        new AlertDialog
                .Builder(new ContextThemeWrapper(this, R.style.Theme_AppCompat))
                .setMessage("Welcome to Storage_SQLite Application")
                .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        toastMessage(options.get(position) + ", " + 1);
        spinnerId = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        toastMessage("Please make a selection!");
    }
}