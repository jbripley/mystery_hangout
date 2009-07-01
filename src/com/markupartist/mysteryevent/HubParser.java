package com.markupartist.mysteryevent;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class HubParser extends DefaultHandler {
    private static final String TAG = "HubParser";
    private ArrayList<Hub> mHubs = new ArrayList<Hub>();
    private String mCurrentText;
    private Hub mCurrentHub = null;

    public ArrayList<Hub> getHubs(InputStream inputStream) {
        mHubs.clear();
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();
            xr.setContentHandler(this);
            InputSource input = new InputSource(inputStream);
            input.setEncoding("UTF-8");
            xr.parse(input);
        } catch (MalformedURLException e) {
            Log.e(TAG, e.toString());
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } catch (SAXException e) {
            Log.e(TAG, e.toString());
        } catch (ParserConfigurationException e) {
            Log.e(TAG, e.toString());
        }

        return mHubs;
    }

    public void startElement(String uri, String name, String qName, Attributes atts) {
        if (name.trim().equals("hub")) {
            mCurrentHub = new Hub();
        }
    }

    public void characters(char ch[], int start, int length) {
        mCurrentText = (new String(ch).substring(start, start + length));
    }

    public void endElement(String uri, String name, String qName)
                throws SAXException {
        if (mCurrentHub != null) {
            if (name.trim().equals("title")) {
                mCurrentHub.setTitle(mCurrentText);
            } else if (name.trim().equals("latitude")) {
                mCurrentHub.setLatitude(mCurrentText);
            } else if (name.trim().equals("longitude")) {
                mCurrentHub.setLongitude(mCurrentText);
            }
        }

        if (name.trim().equals("hub")) {
            Log.d(TAG, "Adding hangout " + mCurrentHub.getTitle());
            mHubs.add(mCurrentHub);
        }
    }
}
