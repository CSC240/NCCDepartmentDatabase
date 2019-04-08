package edu.ncc.nccdepartmentdatabase;
import android.app.ListActivity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class MainActivity extends ListActivity {

    private DepartmentInfoSource datasource;
    private ArrayAdapter<DepartmentEntry> adapter;
    private boolean done = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datasource = new DepartmentInfoSource(this);
        datasource.open();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onClick(View view) {
        DepartmentEntry dept;
        List<DepartmentEntry> values;
        switch (view.getId()) {
            case R.id.createdb:
                new ParseURL().execute();
                break;
            case R.id.usedb:
                done = true;
                break;
            case R.id.show:
                if (done) {
                    values = datasource.getAllDepartments();
                    adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, values);
                    setListAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.query:
                if (done) {
                    values = datasource.findDepartments();
                    adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, values);
                    setListAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
        }
    }

    public void onDestroy()
    {
        datasource.close();
        super.onDestroy();
    }

    private class ParseURL extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String str;
            String deptName;
            String deptPhone;
            String deptLocation;
            Document doc;
            int count = 0;

            try {
                // connect to the webpage
                doc = Jsoup.connect("http://www.ncc.edu/contactus/deptdirectory.shtml").get();

                // find the body of the webpage
                Elements tableEntries = doc.select("tbody");
                for (Element e : tableEntries)
                {
                    // look for a row in the table
                    Elements trs = e.getElementsByTag("tr");

                    // for each element in the row (there are 5)
                    for (Element e2 : trs)
                    {
                        // get the table descriptor
                        Elements tds = e2.getElementsByTag("td");

                        // ignore the first row
                        if (count > 0) {
                            // get the department name and remove the formatting tags
                            deptName = tds.get(0).text();

                            // get the department phone number
                            deptPhone = tds.get(1).text();

                            // get the department location
                            deptLocation = tds.get(4).text();

                            datasource.addDept(deptName, deptPhone, deptLocation);
                        }
                        count++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast toast = Toast.makeText(getApplicationContext(),"Database Created!",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

            done = true;
        }
    }
}