package braude.motorsequence;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.clust4j.algo.KMedoids;
import com.clust4j.algo.KMedoidsParameters;
import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import util.email.CachedFileProvider;
import util.email.GenerateCsv;

public class ClusterResultActivity extends AppCompatActivity {

    private List<Integer> pointColors;
    private String[] groupsSymbol;
    private static Pair<PointsGraphSeries<DataPoint>, TableRow> seletedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cluster_result);

        pointColors = new ArrayList<>(Arrays.asList(Color.BLUE, Color.RED, Color.GREEN, Color.MAGENTA));
        groupsSymbol = getApplicationContext().getResources().getStringArray(R.array.array_groupSymbol);

        //get extras
        Bundle extras = Preconditions.checkNotNull(getIntent().getExtras());
        int k = extras.getInt(getString(R.string.cluster_k));
        int[] clusterAtt = extras.getIntArray(getString(R.string.cluster_clusterAtt));
        int[] graphAtt = extras.getIntArray(getString(R.string.cluster_graphAtt));
        Array2DRowRealMatrix mat = (Array2DRowRealMatrix) getIntent().getSerializableExtra("braude.motorsequence.DATA");
        String[] names = extras.getStringArray("braude.motorsequence.NAMES");


        //clusterring
        boolean autoClustFlag = (k == 0);
        KMedoids km = new KMedoidsParameters(k).fitNewModel(mat);
        int[] dataCluster = km.getLabels();
        double silhouetteScore = km.silhouetteScore();
        for (; k < 4 && autoClustFlag; k++) {
            KMedoids kmTemp = new KMedoidsParameters(k).fitNewModel(mat);
            final int[] resultsTemp = kmTemp.getLabels();
            double rTemp = kmTemp.silhouetteScore();
            if (rTemp > silhouetteScore) {
                km = kmTemp;
                dataCluster = resultsTemp;
                silhouetteScore = rTemp;
            }
        }

        //show cluster in graphview
        //set list of colors
        pointColors = new ArrayList<>(Arrays.asList(Color.BLUE, Color.RED, Color.GREEN, Color.MAGENTA));
        Random random = new Random();
        for (int i = pointColors.size(); i < k; i++) {
            int r = random.nextInt();
            int g = random.nextInt();
            int b = random.nextInt();
            pointColors.add(Color.rgb(r, g, b));
        }

        GraphView graphView = (GraphView) findViewById(R.id.graph_clusterResult_result);
        BiMap<PointsGraphSeries<DataPoint>, TableRow> biMap = HashBiMap.create();

        Point p = new Point(0, 0);
        for (int i = 0; i < mat.getRowDimension(); i++) {
            double x = mat.getEntry(i, 0);
            double y = mat.getEntry(i, 1);
            final PointsGraphSeries<DataPoint> seriesPoint = new PointsGraphSeries<>(new DataPoint[]{new DataPoint(x, y)});
            seriesPoint.setColor(pointColors.get(dataCluster[i]));
            setShape(seriesPoint, groupsSymbol[0]);
            graphView.addSeries(seriesPoint);

            if (x > p.x)
                p.x = (int) Math.ceil(x);
            if (y > p.y)
                p.y = (int) Math.ceil(y);

        }
        p.x += Math.ceil(p.x / 5) + 1;
        p.y += Math.ceil(p.y / 5) + 1;

        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMinY(0);
        graphView.getViewport().setMaxX(p.x);
        graphView.getViewport().setMaxY(p.y);
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setXAxisBoundsManual(true);

        //TODO: repair
        //insert data to perticipant-cluater table
        String[] dataClusterStr = convertIntegerToStringArray(dataCluster);
        TableLayout participantTable = (TableLayout) findViewById(R.id.table_clusterResult_name);
        for (int i = 0; i < mat.getRowDimension(); i++) {
            TableRow row = new TableRow(getApplicationContext());
            row.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
            row.setWeightSum(4);
            makeTextToRow(i, row, names, 3);
            makeTextToRow(i, row, dataClusterStr, 1);

            PointsGraphSeries<DataPoint> series = (PointsGraphSeries<DataPoint>) graphView.getSeries().get(i);
            series.setOnDataPointTapListener(new MyOnDataPointTapListener(graphView, row));
            row.setOnClickListener(new MyOnClickListener(graphView, series));

            biMap.put(series, row);
            participantTable.addView(row);
        }

        //TODO: repair
        //insert data to group-symbol table
        String[] groups = getApplicationContext().getResources().getStringArray(R.array.array_group);
        String[] groups1 = Arrays.copyOfRange(groups, 1, groups.length);
        TableLayout groupTable = (TableLayout) findViewById(R.id.table_clusterResult_group);
        for (int i = 0; i < groupsSymbol.length; i++) {
            TableRow row = new TableRow(getApplicationContext());
            row.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
            row.setWeightSum(4);
            makeTextToRow(i, row, groups1, 3);
            makeTextToRow(i, row, groupsSymbol, 1);

            groupTable.addView(row);
        }

        //set silhouetteScore
        TextView silhouetteText = (TextView) findViewById(R.id.text_clusterResult_silhouetteVal);
//            String htmlString="Silhouette Value: <u>" + Math.round(silhouetteScore * 100.0) / 100.0 + "</u>";
//            silhouetteText.setText(Html.fromHtml(htmlString));
        silhouetteText.setText("Silhouette Value: " + Math.round(silhouetteScore * 100.0) / 100.0);

        //initialize send Cluster Button
        Button send = (Button) findViewById(R.id.button_clusterResult_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail3();
            }
        });

    }

    private String[] convertIntegerToStringArray(int[] dataCluster) {
        String[] dataClusterStr = new String[dataCluster.length];
        for (int i = 0; i < dataCluster.length; i++)
            dataClusterStr[i] = String.valueOf(dataCluster[i]);
        return dataClusterStr;
    }

    private void makeTextToRow(int i, TableRow row, String[] values, int weight) {
        TextView text = new TextView(getApplicationContext());
        text.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT, weight));
        text.setTextAppearance(getApplicationContext(), R.style.tableTextView);
        text.setBackground(getResources().getDrawable(R.drawable.border));
        text.setText(values[i]);
        text.setGravity(Gravity.CENTER);
        row.addView(text);
    }

    private void setShape(PointsGraphSeries<DataPoint> series, final String columnKey) {
        if (columnKey.length() > 1)
            series.setShape(PointsGraphSeries.Shape.valueOf(columnKey));
        else
            series.setCustomShape(new PointsGraphSeries.CustomShape() {
                @Override
                public void draw(Canvas canvas, Paint paint, float x, float y, DataPointInterface dataPoint) {
                    paint.setTextSize(25);
                    canvas.drawText(columnKey, x - 7, y + 7, paint);
                }
            });
    }

    private class MyOnDataPointTapListener implements OnDataPointTapListener {
        private TableRow row;
        private GraphView graphView;

        public MyOnDataPointTapListener(GraphView graphView, TableRow row) {
            this.row = row;
            this.graphView = graphView;
        }

        @Override
        public void onTap(Series seriesTap, DataPointInterface dataPoint) {
            PointsGraphSeries<DataPoint> series = (PointsGraphSeries<DataPoint>) seriesTap;
            clickAction(graphView, series, row);
        }
    }

    private class MyOnClickListener implements View.OnClickListener {
        private GraphView graphView;
        private PointsGraphSeries<DataPoint> series;

        public MyOnClickListener(GraphView graphView, PointsGraphSeries<DataPoint> series) {
            this.graphView = graphView;
            this.series = series;
        }

        @Override
        public void onClick(View v) {
            TableRow row = (TableRow) v;
            clickAction(graphView, series, row);
        }
    }

    private void clickAction(GraphView graphView, PointsGraphSeries<DataPoint> series, TableRow row) {
        if (seletedData != null) {
            seletedData.first.setColor(Color.argb(255,
                    Color.red(seletedData.first.getColor()),
                    Color.green(seletedData.first.getColor()),
                    Color.blue(seletedData.first.getColor())));
            seletedData.second.setBackgroundColor(Color.WHITE);
        }
        seletedData = new Pair<>(series, row);
        int color = Color.argb(100,
                Color.red(series.getColor()),
                Color.green(series.getColor()),
                Color.blue(series.getColor()));
        series.setColor(color);
        row.setBackgroundColor(color);
        graphView.invalidate();
    }

    private void sendEmail() {
        String content = "Hello Wourld!";
        File file = null;
        FileOutputStream fos = null;
        String fileName = null;
        try {
            fileName = "data";
            file = File.createTempFile(fileName, ".txt", this.getCacheDir());
            fos = new FileOutputStream(file);
            // get the content in bytes
            byte[] contentInBytes = content.getBytes();
            fos.write(contentInBytes);
            fos.flush();
            fos.close();

            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{"asaf11108@gmail.com"});
            email.putExtra(Intent.EXTRA_SUBJECT, "subject");
            email.putExtra(Intent.EXTRA_TEXT, "message");
            // the attachment
            email.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            //need this to prompts email client only
            email.setType("message/rfc822");

            startActivity(Intent.createChooser(email, "Choose an Email client :"));

//            boolean del = this.deleteFile(fileName);
        } catch (IOException e) {
            // Error while creating file
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


//        try {
//            Intent intent = new Intent(Intent.ACTION_SENDTO);
//            intent.setType("text/plain");
//            intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
//            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse( "file://"+filelocation));
//            intent.putExtra(Intent.EXTRA_TEXT, "message");
//            intent.setData(Uri.parse("mailto:asaf11108@gmail.com"));
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            startActivity(intent);
//        } catch(Exception e)  {
//            System.out.println("is exception raises during sending mail"+e);
//        }

//        String filename="contacts_sid.vcf";
//        File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
//        Uri path = Uri.fromFile(filelocation);
//
//        Intent emailIntent = new Intent(Intent.ACTION_SEND);
// set the type to 'email'
//        emailIntent .setType("vnd.android.cursor.dir/email");
//        String to[] = {"asaf11108@gmail.com"};
//        emailIntent .putExtra(Intent.EXTRA_EMAIL, to);
// the attachment
//        emailIntent .putExtra(Intent.EXTRA_STREAM, path);
// the mail subject
//        emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Subject");
//        startActivity(Intent.createChooser(emailIntent , "Send email..."));
    }

    private void sendEmail2() {
        String to = "asaf11108@gmail.com";
        String subject = "sub1";
        String message = "m";

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("plain/text");
        File data = null;

        try {
            Date dateVal = new Date();
            String filename = dateVal.toString();
            data = File.createTempFile("Report", ".csv");
            FileWriter out = (FileWriter) GenerateCsv.generateCsvFile(
                    data, "Name,Data1");
            i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(data));
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
            i.putExtra(Intent.EXTRA_SUBJECT, subject);
            i.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(i, "E-mail"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void createCachedFile(Context context, String fileName, String content) throws IOException {

        File cacheFile = new File(context.getCacheDir() + File.separator + fileName);
        cacheFile.createNewFile();

        FileOutputStream fos = new FileOutputStream(cacheFile);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF8");
        PrintWriter pw = new PrintWriter(osw);

        pw.println(content);

        pw.flush();
        pw.close();
    }

    public static Intent getSendEmailIntent(Context context, String email, String subject, String body, String fileName) {

        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        //Explicitly only use Gmail to send
        emailIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");

        emailIntent.setType("plain/text");

        //Add the recipients
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});

        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);

        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);

        //Add the attachment by specifying a reference to our custom ContentProvider
        //and the specific file of interest
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://" + CachedFileProvider.AUTHORITY + "/" + fileName));

        return emailIntent;
    }

    private void sendEmail3() {
        try {
            createCachedFile(ClusterResultActivity.this, "Test.txt", "This is a test");

            startActivity(getSendEmailIntent(ClusterResultActivity.this, "asaf11108@gmail.com", "Test", "See attached", "Test.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ActivityNotFoundException e) {
            Toast.makeText(ClusterResultActivity.this, "Gmail is not available on this device.", Toast.LENGTH_SHORT).show();
        }
    }
}
