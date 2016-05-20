package com.polybookshare.master.campusbookshare;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookshelfActivity extends Fragment {
    private String cookie ="";
    private int actualPosition = 0;
    private BookAdapter m_book_adapter;
    private ArrayList<Book> m_books;
    private RecyclerView bookList;
    private View dialogView;
    private Paint p = new Paint();
    private Spinner condition_spinner;
    private EditText edit_notes;
    private EditText edit_price;
    private RequestQueue queue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.cookie = getArguments().getString("cookies");
        View view = inflater.inflate(R.layout.activity_bookshelf, container, false);

         queue = Volley.newRequestQueue(view.getContext());
        m_books = new ArrayList<Book>();
        bookList = (RecyclerView) view.findViewById(R.id.bookshelf);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        bookList.setLayoutManager(layoutManager);
        this.m_book_adapter = new BookAdapter(m_books);

        bookList.setAdapter(this.m_book_adapter);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.add_book);
        fab.setBackgroundTintList(getResources().getColorStateList(R.color.buttonsColor));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAddBookDialog();
            }
        });

        initSwipe();
        getMyBooks(view);


        return view;
    }



    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                    String id = m_books.get(position).getId();

                    deleteBook(id, position);

                } else {


                    LayoutInflater li = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                    dialogView = li.inflate(R.layout.dialog_edit_book, null);

                    condition_spinner = (Spinner) dialogView.findViewById(R.id.edit_condition);
                    edit_notes = (EditText) dialogView.findViewById(R.id.edit_notes);
                    edit_price = (EditText) dialogView.findViewById(R.id.edit_price);
                    alertDialog.setView(dialogView);

                    alertDialog.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updateBooks(m_books.get(position).getId(),
                                    String.valueOf(edit_price.getText()) ,
                                    condition_spinner.getSelectedItem().toString(),
                                    String.valueOf(edit_notes.getText()), position);

                        }
                    });

                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            m_book_adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();

                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_edit_white);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_white_delete);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(bookList);
    }
    private void removeView(){
        if(dialogView != null && dialogView.getParent()!= null) {
            ((ViewGroup) dialogView.getParent()).removeView(dialogView);
        }
    }
    public void getMyBooks(View view) {
        String url = "http://192.168.43.162:8080/api/book/";

        JsonArrayRequest req = new JsonArrayRequest(url,
                new com.android.volley.Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {


                        try {

                            for(int i = 0; i < response.length(); i++) {
                                //int number, String dep, String professor
                                // String lastReview, int comments, int files, int reviews)

                                JSONObject obj = (JSONObject) response.get(i);
                                JSONObject info = (JSONObject) obj.get("googleInfo");


                                JSONObject volume = (JSONObject) info.get("volumeInfo");
                                JSONObject imageLinks = (JSONObject) volume.get("imageLinks");
                                String smallThumbnail = (String) imageLinks.get("smallThumbnail");

                                JSONArray authors = (JSONArray) volume.get("authors");
                                String fullAuthors = "";
                                String title = (String) volume.get("title");
                                String notes = "";


                                if (obj.has("notes"))
                                    notes = (String) obj.get("notes");
                                else
                                    notes = "No description";
                                String condition = (String) obj.get("condition");
                                //double price = (double) obj.get("price");
                                for (int j = 0; j < authors.length(); j++) {
                                    fullAuthors += authors.get(j) + " ";
                                }
                                Log.d("id get books", (String) obj.get("_id"));
                                //String author, String title, String condition, String description,
                                //  String cover_url);
                                m_books.add(new Book ((String) obj.get("_id") ,
                                        fullAuthors, title, condition, notes,
                                        String.valueOf(obj.get("price")), smallThumbnail));
                            }

                            Log.d("finished", "finished");

                            m_book_adapter.notifyDataSetChanged();

                        }
                        catch(JSONException e) {

                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("error64", String.valueOf(error.networkResponse.statusCode));

                //pDialog.dismiss();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String, String>();
                headers.put("Cookie", cookie);
                return headers;
            }
        };

        queue.add(req);
    }


    public void updateBooks(String id, final String price, final String condition, final String notes, final int position) {

        String url = "http://192.168.43.162:8080/api/book/id/" + id;
        Log.d("id update ", id);
        StringRequest req = new StringRequest(Request.Method.PUT, url,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(condition.length() != 0) {
                    m_books.get(position).setCondition(condition);
                }

                if (price.length() !=0) {
                    m_books.get(position).setPrice(price);
                }

                if (notes.length() != 0) {
                    m_books.get(position).setDescription(notes);
                }
                m_book_adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("update error", error.getMessage());
            }
        })  {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //add params <key,value>

                if(condition.length() != 0) {
                    params.put("condition", condition);
                }

                if (price.length() !=0) {
                    params.put("price", price);
                }

                if (notes.length() != 0) {
                    params.put("notes", notes);
                }
                //m_book_adapter.notifyDataSetChanged();
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String, String>();
                headers.put("Cookie", cookie);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };


        queue.add(req);

    }

    public void deleteBook(String id, int position) {
        String url = "http://192.168.43.162:8080/api/book/id/" + id;
        final int pos = position;
        StringRequest req = new StringRequest(Request.Method.DELETE, url,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                m_books.remove(pos);
                m_book_adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("delete error", error.getMessage());


            }
        })  {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String, String>();
                headers.put("Cookie", cookie);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };


        queue.add(req);
    }

    public void createAddBookDialog() {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        dialogView = li.inflate(R.layout.dialog_add_book, null);

        final EditText isbn = (EditText) dialogView.findViewById(R.id.add_isbn);
        edit_price = (EditText) dialogView.findViewById(R.id.add_price);
        condition_spinner = (Spinner) dialogView.findViewById(R.id.add_condition);
         edit_notes = (EditText) dialogView.findViewById(R.id.add_notes);
        alertDialog.setView(dialogView);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Add Textbook", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_book_adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isbn.getText().length() == 0 || edit_price.getText().length() == 0
                        || condition_spinner.getSelectedItem().toString().length() == 0 &&
                        edit_notes.getText().length() == 0) {

                    if (isbn.getText().length() == 0)
                        isbn.setError("Field cannot be empty");
                    if (edit_price.getText().length() == 0)
                        edit_price.setError("Field cannot be empty");
                    if (condition_spinner.getSelectedItem().toString().length() == 0) {
                        TextView error = (TextView) condition_spinner.getSelectedView();
                        error.setError("Empty");
                        error.setTextColor(Color.RED);
                        error.setText("Field cannot be empty");
                    }
                    if (edit_notes.getText().length() == 0) {
                        edit_notes.setError("Field cannot be empty");
                    }

                }

                else {
                    try {
                        addTextBook(isbn.getText().toString(), edit_price.getText().toString(),
                                condition_spinner.getSelectedItem().toString(),
                                edit_notes.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    alertDialog.dismiss();
                }
            }
        });
    }

    public void addTextBook(final String isbn, final String price, final String condition, final String notes) throws JSONException {
        String url = "http://192.168.43.162:8080/api/book/";

        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);

                    Log.d("json response add", json.toString());
                    JSONObject info = (JSONObject) json.get("googleInfo");

                    JSONObject volume = (JSONObject) info.get("volumeInfo");
                    JSONObject imageLinks = (JSONObject) volume.get("imageLinks");
                    String smallThumbnail = (String) imageLinks.get("smallThumbnail");

                    JSONArray authors = (JSONArray) volume.get("authors");
                    String fullAuthors = "";
                    String title = (String) volume.get("title");
                    String notes = (String) json.get("notes");

                    String condition = (String) json.get("condition");

                    for (int j = 0; j < authors.length(); j++) {

                        fullAuthors += authors.get(j) + " ";

                    }

                    m_books.add(new Book ((String) json.get("_id") ,
                        fullAuthors, title, condition, notes,
                        String.valueOf(json.get("price")), smallThumbnail));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                m_book_adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null && error.getMessage() != null) {
                    if (error.networkResponse != null) {
                        if (error.networkResponse.statusCode == 500);
                            Toast.makeText(getActivity(), "Internal server error", Toast.LENGTH_LONG).show();
                        if (error.networkResponse.statusCode == 401);
                        Toast.makeText(getActivity(), "Unauthorized", Toast.LENGTH_LONG).show();
                        if (error.networkResponse.statusCode == 404);
                        Toast.makeText(getActivity(), "Wrong crendials", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Cookie", cookie);
                return headers;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //add params <key,value>
                params.put("isbn", isbn);
                params.put("condition", condition);
                params.put("price", price);
                params.put("notes", notes);

                return params;
            }

        };

        queue.add(req);
    }
}
