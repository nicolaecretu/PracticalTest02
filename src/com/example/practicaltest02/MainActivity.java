package com.example.practicaltest02;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

	
	EditText word_to_search = null;
	Button send = null;
	EditText answer_text = null;
	
	private ServerThread serverThread       = null;
	private Client clientThread             = null;
	
	
	private GetWeatherForecastButtonClickListener getWeatherForecastButtonClickListener = new GetWeatherForecastButtonClickListener();
	private class GetWeatherForecastButtonClickListener implements Button.OnClickListener {
		
		@Override
		public void onClick(View view) {
			
			serverThread = new ServerThread(5645);
			
			//String clientAddress = clientAddressEditText.getText().toString();
			String clientText    = word_to_search.getText().toString();
			
			if (clientText == null ) {
				Toast.makeText(
					getApplicationContext(),
					"Client connection parameters should be filled!",
					Toast.LENGTH_SHORT
				).show();
				return;
			}
			
			
			
			
			//weatherForecastTextView.setText(Constants.EMPTY_STRING);
			
			clientThread = new Client(
					clientText);
			clientThread.start();
		}
	}
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        word_to_search = (EditText)findViewById(R.id.text_to_send);
        send = (Button)findViewById(R.id.Send_button);
        answer_text = (EditText)findViewById(R.id.Text_received);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
