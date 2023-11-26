package edu.uiuc.cs427app;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MapsActivityTest {

    @Mock
    Context mockContext;

    @Mock
    GoogleMap mockGoogleMap;

    @Mock
    RequestQueue mockRequestQueue;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGMap() {
        MapsActivity mapsActivity = mock(MapsActivity.class);

        when(mockContext.getApplicationContext()).thenReturn(mockContext);
        when(mapsActivity.getApplicationContext()).thenReturn(mockContext);
        when(mockRequestQueue.add(Mockito.any())).thenReturn(null);

        mapsActivity.LatLon("Champaign", mockGoogleMap, mockContext);

        verify(mapsActivity).LatLon("Champaign", mockGoogleMap, mockContext);
        verify(mockGoogleMap).addMarker(new MarkerOptions().position(new LatLng(40.1164, -88.2434)).title("Champaign - Lat: 40.1164 - Lon: -88.2434"));
        verify(mockGoogleMap).moveCamera(Mockito.any());
        verify(mockGoogleMap).animateCamera(Mockito.any());
    }
}