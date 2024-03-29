package com.android.gallery3d.filtershow.filters;

import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import com.android.gallery3d.R;
import com.android.gallery3d.filtershow.imageshow.ControlPoint;
import com.android.gallery3d.filtershow.imageshow.Spline;

import java.io.IOException;

/**
 * TODO: Insert description here. (generated by hoford)
 */
public class FilterCurvesRepresentation extends FilterRepresentation {
    private static final String LOGTAG = "FilterCurvesRepresentation";
    public static final String SERIALIZATION_NAME = "Curve";
    private static final int MAX_SPLINE_NUMBER = 4;

    private Spline[] mSplines = new Spline[MAX_SPLINE_NUMBER];

    public FilterCurvesRepresentation() {
        super("Curves");
        setSerializationName("CURVES");
        setFilterClass(ImageFilterCurves.class);
        setTextId(R.string.curvesRGB);
        setOverlayId(R.drawable.filtershow_button_colors_curve);
        setEditorId(R.id.imageCurves);
        setShowParameterValue(false);
        setSupportsPartialRendering(true);
        reset();
    }

    @Override
    public FilterRepresentation copy() {
        FilterCurvesRepresentation representation = new FilterCurvesRepresentation();
        copyAllParameters(representation);
        return representation;
    }

    @Override
    protected void copyAllParameters(FilterRepresentation representation) {
        super.copyAllParameters(representation);
        representation.useParametersFrom(this);
    }

    @Override
    public void useParametersFrom(FilterRepresentation a) {
        if (!(a instanceof FilterCurvesRepresentation)) {
            Log.v(LOGTAG, "cannot use parameters from " + a);
            return;
        }
        FilterCurvesRepresentation representation = (FilterCurvesRepresentation) a;
        Spline[] spline = new Spline[MAX_SPLINE_NUMBER];
        for (int i = 0; i < spline.length; i++) {
            Spline sp = representation.mSplines[i];
            if (sp != null) {
                spline[i] = new Spline(sp);
            } else {
                spline[i] = new Spline();
            }
        }
        mSplines = spline;
    }

    @Override
    public boolean isNil() {
        for (int i = 0; i < MAX_SPLINE_NUMBER; i++) {
            if (getSpline(i) != null && !getSpline(i).isOriginal()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(FilterRepresentation representation) {
        if (!super.equals(representation)) {
            return false;
        }

        if (!(representation instanceof FilterCurvesRepresentation)) {
            return false;
        } else {
            FilterCurvesRepresentation curve =
                    (FilterCurvesRepresentation) representation;
            for (int i = 0; i < MAX_SPLINE_NUMBER; i++) {
                if (!getSpline(i).sameValues(curve.getSpline(i))) {
                    return false;
                }
            }
        }
        // Every spline matches, therefore they are the same.
        return true;
    }

    public void reset() {
        Spline spline = new Spline();

        spline.addPoint(0.0f, 1.0f);
        spline.addPoint(1.0f, 0.0f);

        for (int i = 0; i < MAX_SPLINE_NUMBER; i++) {
            mSplines[i] = new Spline(spline);
        }
    }

    public void setSpline(int splineIndex, Spline s) {
        mSplines[splineIndex] = s;
    }

    public Spline getSpline(int splineIndex) {
        return mSplines[splineIndex];
    }

    @Override
    public void serializeRepresentation(JsonWriter writer) throws IOException {
        writer.beginObject();
        {
            writer.name(NAME_TAG);
            writer.value(getName());
            for (int i = 0; i < mSplines.length; i++) {
                writer.name(SERIALIZATION_NAME + i);
                writer.beginArray();
                int nop = mSplines[i].getNbPoints();
                for (int j = 0; j < nop; j++) {
                    ControlPoint p = mSplines[i].getPoint(j);
                    writer.beginArray();
                    writer.value(p.x);
                    writer.value(p.y);
                    writer.endArray();
                }
                writer.endArray();
            }

        }
        writer.endObject();
    }

    @Override
    public void deSerializeRepresentation(JsonReader sreader) throws IOException {
        sreader.beginObject();
        Spline[] spline = new Spline[MAX_SPLINE_NUMBER];
        while (sreader.hasNext()) {
            String name = sreader.nextName();
            if (NAME_TAG.equals(name)) {
                setName(sreader.nextString());
            } else if (name.startsWith(SERIALIZATION_NAME)) {
                int curveNo = Integer.parseInt(name.substring(SERIALIZATION_NAME.length()));
                spline[curveNo] = new Spline();
                sreader.beginArray();
                while (sreader.hasNext()) {
                    sreader.beginArray();
                    sreader.hasNext();
                    float x = (float) sreader.nextDouble();
                    sreader.hasNext();
                    float y = (float) sreader.nextDouble();
                    sreader.endArray();
                    spline[curveNo].addPoint(x, y);
                }
                sreader.endArray();

            }
        }
        mSplines = spline;
        sreader.endObject();
    }

}
