package com.anony.minions.qapoll;

import com.anony.minions.qapoll.data.Instructor;
import com.anony.minions.qapoll.data.Student;
import com.anony.minions.qapoll.data.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		Button instructor = (Button) findViewById(R.id.instructor_login);
		instructor.setOnClickListener( new OnClickListener() {

			View viewInstructor = getLayoutInflater().inflate(R.layout.create_room, null);

			@Override
			public void onClick(View arg0) {
				DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// overridden later
					}
				};
				DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				};

				final AlertDialog ad = new AlertDialog.Builder( LoginActivity.this )
				.setView(viewInstructor)
				.setPositiveButton( "Create", positiveListener )
				.setNegativeButton( "Cancel", negativeListener )
				.create();

				ad.show();

				ad.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener( new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if(viewInstructor != null) {
							EditText room = (EditText) viewInstructor.findViewById(R.id.instructor_new_room);
							String r = room.getText().toString();

							if( r.length() == 0 ) {
								String message = "Please enter a room name.";
								Toast t = Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT);
								t.show();
								return;
							}
							if( !roomAvailable(r) ) {
								String message = "Room name taken. Please choose another.";
								Toast t = Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT);
								t.show();
								return;
							}
							if( !validateUser() ) {
								// message user info invalid
								return;
							}
							if( !validateRoom() ) {
								// message room invalid
								return;
							}

							// connection is ready to go
							ad.dismiss();
							User newUser = new Instructor();
							newUser.setId("instructor");

							Intent i = new Intent( LoginActivity.this, QustionListActivity.class );
							i.putExtra("user", "instructor");
							i.putExtra("room", r);
							startActivity(i);
						}
					}
				});
			}
		} );

		Button student = (Button) findViewById(R.id.student_login);
		student.setOnClickListener( new OnClickListener() {

			View viewStudent = getLayoutInflater().inflate(R.layout.join_room, null);

			@Override
			public void onClick(View arg0) {
				DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// overridden later
					}
				};
				DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				};

				final AlertDialog ad = new AlertDialog.Builder( LoginActivity.this )
				.setView(viewStudent)
				.setPositiveButton( "Login", positiveListener )
				.setNegativeButton( "Cancel", negativeListener )
				.create();

				ad.show();

				ad.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener( new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if( viewStudent != null ) {
							EditText user = (EditText) viewStudent.findViewById(R.id.username);
							EditText pass = (EditText) viewStudent.findViewById(R.id.password);
							EditText room = (EditText) viewStudent.findViewById(R.id.room);

							String u = user.getText().toString();
							String p = pass.getText().toString();
							String r = room.getText().toString();

							if( !formsFilled( u, p, r ) ) {
								String message = "Please complete each field before continuing.";
								Toast t = Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT);
								t.show();
								return;
							}
							if( !validateUser() ) {
								// message user info invalid
								return;
							}
							if( !validateRoom() ) {
								// message room invalid
								return;
							}

							// connection is ready to go
							ad.dismiss();
							User newUser = new Student();
							newUser.setId(u);

							Intent i = new Intent( LoginActivity.this, QustionListActivity.class );
							i.putExtra("user", "student");
							i.putExtra("id", u);
							i.putExtra("room", r);
							startActivity(i);
						}
					}
				});

			}
		} );
	}

	private boolean validateUser() {
		return true;
	}

	private boolean validateRoom() {
		return true;
	}

	private boolean formsFilled( String user, String pass, String room ) {
		return !( user.length() * pass.length() * room.length() == 0 );
	}

	private boolean roomAvailable( String roomName ) {
		return true;
	}
}