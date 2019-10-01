package com.sidoarjolaptopservice.dashboard_donatur;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.sidoarjolaptopservice.masjid.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PaymentOnlineActivity extends AppCompatActivity {
    Button btn_download;
    FileOutputStream outStream;
    ImageView qr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_online);
        btn_download=findViewById(R.id.download);
        qr=findViewById(R.id.qr);
        btn_download.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Drawable drawable = getDrawable(R.drawable.qr_dana);

                // Get the bitmap from drawable object
                Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();

                /*
                    ContextWrapper
                        Proxying implementation of Context that simply delegates all of its calls
                        to another Context. Can be subclassed to modify behavior without
                        changing the original Context.
                */
                ContextWrapper wrapper = new ContextWrapper(getApplicationContext());

                /*
                    File
                        An "abstract" representation of a file system entity identified by a
                        pathname. The pathname may be absolute (relative to the root directory
                        of the file system) or relative to the current directory in which
                        the program is running.

                        The actual file referenced by a File may or may not exist. It may also,
                        despite the name File, be a directory or other non-regular file.
                */

                /*
                    public File getDir (String name, int mode)
                        Retrieve, creating if needed, a new directory in which the application can
                        place its own custom data files. You can use the returned File object to
                        create and access files in this directory. Note that files created through
                        a File object will only be accessible by your own application; you can only
                        set the mode of the entire directory, not of individual files.

                        Parameters
                        name : Name of the directory to retrieve. This is a directory
                            that is created as part of your application data.
                        mode : Operating mode. Use 0 or MODE_PRIVATE for the default operation,
                            MODE_WORLD_READABLE and MODE_WORLD_WRITEABLE to control permissions.

                        Returns
                            A File object for the requested directory. The directory will
                            have been created if it does not already exist.
                */

                /*
                    public static final int MODE_PRIVATE
                        File creation mode: the default mode, where the created file can only be
                        accessed by the calling application (or all applications sharing
                        the same user ID).
                */

                // Initializing a new file
                // The bellow line return a directory in internal storage
                File file = wrapper.getDir("Images",MODE_PRIVATE);

                /*
                    public File (String dirPath, String name)
                        Constructs a new File using the specified directory path and file name,
                        placing a path separator between the two.

                        Parameters
                            dirPath : the path to the directory where the file is stored.
                            name : the file's name.

                        Throws
                            NullPointerException if name == null.
                */

                // Create a file to save the image
                file = new File(file, "UniqueFileName"+".jpg");

                try{
                    /*
                        OutputStream
                            A writable sink for bytes.

                            Most clients will use output streams that write data to the file system
                            (FileOutputStream), the network (getOutputStream()/getOutputStream()),
                            or to an in-memory byte array (ByteArrayOutputStream).

                            Use OutputStreamWriter to adapt a byte stream like this one into a
                            character stream.
                    */
                    OutputStream stream = null;

                    /*
                        FileOutputStream
                            An output stream that writes bytes to a file. If the output file exists,
                            it can be replaced or appended to. If it does not exist, a new
                            file will be created.
                    */

                    /*
                        public FileOutputStream (File file)
                            Constructs a new FileOutputStream that writes to file. The file will be
                            truncated if it exists, and created if it doesn't exist.

                            Throws
                                FileNotFoundException : if file cannot be opened for writing.
                    */

                    stream = new FileOutputStream(file);

                    /*
                        public boolean compress (Bitmap.CompressFormat format, int quality, OutputStream stream)
                            Write a compressed version of the bitmap to the specified outputstream.
                            If this returns true, the bitmap can be reconstructed by passing a
                            corresponding inputstream to BitmapFactory.decodeStream().

                            Note: not all Formats support all bitmap configs directly, so it is
                            possible that the returned bitmap from BitmapFactory could be in a
                            different bitdepth, and/or may have lost per-pixel alpha
                            (e.g. JPEG only supports opaque pixels).

                            Parameters
                                format : The format of the compressed image
                                quality : Hint to the compressor, 0-100. 0 meaning compress for small
                                    size, 100 meaning compress for max quality. Some formats,
                                    like PNG which is lossless, will ignore the quality setting
                                stream : The outputstream to write the compressed data.
                            Returns
                                true : if successfully compressed to the specified stream.
                    */
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);

                    /*
                        public void flush ()
                            Flushes this stream. Implementations of this method should ensure
                            that any buffered data is written out. This implementation does nothing.

                            Throws
                                IOException : if an error occurs while flushing this stream.
                    */
                    stream.flush();

                    /*
                        public void close ()
                            Closes this stream. Implementations of this method should free any
                            resources used by the stream. This implementation does nothing.

                            Throws
                                IOException : if an error occurs while closing this stream.
                    */
                    stream.close();

                }catch (IOException e) // Catch the exception
                {
                    e.printStackTrace();
                }
                Uri savedImageURI = Uri.parse(file.getAbsolutePath());

                // Display the saved image to ImageView
                qr.setImageURI(savedImageURI);

                // Display saved image uri to TextView
               Toast.makeText(getApplicationContext(), String.valueOf(savedImageURI),Toast.LENGTH_SHORT).show();
            }
        });
    }


}
