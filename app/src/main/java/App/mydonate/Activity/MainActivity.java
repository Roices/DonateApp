package App.mydonate.Activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

import App.mydonate.API.DonationApi;
import App.mydonate.Model.Donation;
import App.mydonate.R;


public class MainActivity extends Base {

    private NumberPicker numberPicker;
    private Button button;
    private RadioGroup radioGroup;
    private ProgressBar progressBar;
    private RadioButton buttonPaypal;
    private EditText amountText;
    public static TextView amountTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnhXa();

        numberPicker.setMaxValue(1000);
        numberPicker.setMinValue(0);
        numberPicker.setValue(0);

        progressBar.setMax(10000);
        amountTotal.setText("$" + app.totalDonated);
        buttonPaypal.setChecked(true);

        buttonPress();

    }

    public void buttonPress(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String method = radioGroup.getCheckedRadioButtonId() == R.id.radioPaypal ? "PayPal" : "Direct";
                int donatedAmount =  numberPicker.getValue();
                if (donatedAmount == 0)
                {
                    String text = amountText.getText().toString();
                    if (!text.equals(""))
                        donatedAmount = Integer.parseInt(text);
                }
                if (donatedAmount > 0)
                {
                    app.newDonation(new Donation(donatedAmount, method));
                    progressBar.setProgress(app.totalDonated);
                    String totalDonatedStr = "$" + app.totalDonated;
                    amountTotal.setText(totalDonatedStr);
                }
            }
        });
    }

    private class GetAllTask extends AsyncTask<String, Void, List<Donation>> {
        protected ProgressDialog dialog;
        protected Context context;
        public GetAllTask(Context context)
        {
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog = new ProgressDialog(context, 1);
            this.dialog.setMessage("Retrieving Donations List");
            this.dialog.show();
        }
        @Override
        protected List<Donation> doInBackground(String... params) {
            try {
                Log.v("donate", "Donation App Getting All Donations");
                return (List<Donation>) DonationApi.getAll((String) params[0]);
            }
            catch (Exception e) {
                Log.v("donate", "ERROR : " + e);
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(List<Donation> result) {
            super.onPostExecute(result);
            //use result to calculate the totalDonated amount here
            progressBar.setProgress(app.totalDonated);
            amountTotal.setText("$" + app.totalDonated);
            if (dialog.isShowing())
                dialog.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new GetAllTask(this).execute("/donations");
    }

    private void AnhXa() {
        numberPicker = (NumberPicker) findViewById(R.id.numbersPicker);
        button = (Button) findViewById(R.id.buton);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        buttonPaypal = (RadioButton) findViewById(R.id.radioPaypal);
        amountText = (EditText) findViewById(R.id.paymentAmount);
        amountTotal = (TextView) findViewById(R.id.totalSoFar);
    }
}