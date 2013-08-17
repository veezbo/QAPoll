package com.anony.minions.qapoll;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		Button instructor = (Button) findViewById(R.id.instructor_login);
		instructor.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent i = new Intent( LoginActivity.this, QustionListActivity.class );
						startActivity(i);
					}
				};
				DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				};
				
				AlertDialog ad = new AlertDialog.Builder( LoginActivity.this )
				.setView( getLayoutInflater().inflate(R.layout.create_room, null) )
				.setPositiveButton( "Create", positiveListener )
				.setNegativeButton( "Cancel", negativeListener )
				.create();
				
				ad.show();	
			}
		} );
		
		Button student = (Button) findViewById(R.id.student_login);
		student.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if( !validateUser() ) {
							// message user info invalid
						}
						if( !validateRoom() ) {
							// message room invalid
						}
						
						// connection ready to go
						Intent i = new Intent( LoginActivity.this, QustionListActivity.class );
						startActivity(i);
					}
				};
				DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				};
				
				AlertDialog ad = new AlertDialog.Builder( LoginActivity.this )
				.setView( getLayoutInflater().inflate(R.layout.join_room, null) )
				.setPositiveButton( "Login", positiveListener )
				.setNegativeButton( "Cancel", negativeListener )
				.create();
				
				ad.show();
			}
		} );
	}

	private boolean validateUser() {
		return false;
	}
	
	private boolean validateRoom() {
		return false;
	}
}