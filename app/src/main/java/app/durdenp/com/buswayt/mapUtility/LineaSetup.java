package app.durdenp.com.buswayt.mapUtility;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.StringTokenizer;

import app.durdenp.com.buswayt.jsonWrapper.LineaRequestedWrapper;

/**
 * Created by Marco on 28/05/2015.
 */
public class LineaSetup {

    /**
     * Default constructor
     */
    public LineaSetup() {
    }


    /**
     * Add PCoord and FCoord to the LineaDescriptor
     * @param linea
     * @param lineaWrapper
     */
    public void parseLineaRequestedWrapper(LineaDescriptor linea, LineaRequestedWrapper lineaWrapper){
        HashMap<Integer, LinkedList<LatLng>> tmpPcoord = new HashMap();
        HashMap<Integer, LinkedList<LatLng>> tmpFcoord = new HashMap();

        String tmpParsedString = lineaWrapper.getP0();

        if(tmpParsedString.compareTo("none") != 0){
            tmpPcoord.put(0, parseCoordString(tmpParsedString));

        }

        tmpParsedString = lineaWrapper.getP1();

        if(tmpParsedString.compareTo("none") != 0){
            tmpPcoord.put(1, parseCoordString(tmpParsedString));
        }

        tmpParsedString = lineaWrapper.getP2();

        if(tmpParsedString.compareTo("none") != 0){
            tmpPcoord.put(2, parseCoordString(tmpParsedString));
        }

        tmpParsedString = lineaWrapper.getP3();

        if(tmpParsedString.compareTo("none") != 0){
            tmpPcoord.put(3, parseCoordString(tmpParsedString));
        }

        tmpParsedString = lineaWrapper.getP4();

        if(tmpParsedString.compareTo("none") != 0){
            tmpPcoord.put(4, parseCoordString(tmpParsedString));
        }

        tmpParsedString = lineaWrapper.getP5();

        if(tmpParsedString.compareTo("none") != 0){
            tmpPcoord.put(5, parseCoordString(tmpParsedString));
        }

        tmpParsedString = lineaWrapper.getP6();

        if(tmpParsedString.compareTo("none") != 0){
            tmpPcoord.put(6, parseCoordString(tmpParsedString));
        }

        tmpParsedString = lineaWrapper.getP7();

        if(tmpParsedString.compareTo("none") != 0){
            tmpPcoord.put(7, parseCoordString(tmpParsedString));
        }

        tmpParsedString = lineaWrapper.getP8();

        if(tmpParsedString.compareTo("none") != 0){
            tmpPcoord.put(8, parseCoordString(tmpParsedString));
        }

        tmpParsedString = lineaWrapper.getP9();

        if(tmpParsedString.compareTo("none") != 0){
            tmpPcoord.put(9, parseCoordString(tmpParsedString));
        }



        //F String processing

        tmpParsedString = lineaWrapper.getF0();

        if(tmpParsedString.compareTo("none") != 0){
            tmpFcoord.put(0, parseCoordString(tmpParsedString));
        }

        tmpParsedString = lineaWrapper.getF1();

        if(tmpParsedString.compareTo("none") != 0){
            tmpFcoord.put(1, parseCoordString(tmpParsedString));
        }

        tmpParsedString = lineaWrapper.getF2();

        if(tmpParsedString.compareTo("none") != 0){
            tmpFcoord.put(2, parseCoordString(tmpParsedString));
        }

        tmpParsedString = lineaWrapper.getF3();

        if(tmpParsedString.compareTo("none") != 0){
            tmpFcoord.put(3, parseCoordString(tmpParsedString));
        }

        tmpParsedString = lineaWrapper.getF4();

        if(tmpParsedString.compareTo("none") != 0){
            tmpFcoord.put(4, parseCoordString(tmpParsedString));
        }

        tmpParsedString = lineaWrapper.getF5();

        if(tmpParsedString.compareTo("none") != 0){
            tmpFcoord.put(5, parseCoordString(tmpParsedString));
        }

        tmpParsedString = lineaWrapper.getF6();

        if(tmpParsedString.compareTo("none") != 0){
            tmpFcoord.put(6, parseCoordString(tmpParsedString));
        }

        tmpParsedString = lineaWrapper.getF7();

        if(tmpParsedString.compareTo("none") != 0){
            tmpFcoord.put(7, parseCoordString(tmpParsedString));
        }

        tmpParsedString = lineaWrapper.getF8();

        if(tmpParsedString.compareTo("none") != 0){
            tmpFcoord.put(8, parseCoordString(tmpParsedString));
        }

        tmpParsedString = lineaWrapper.getF9();

        if(tmpParsedString.compareTo("none") != 0){
            tmpFcoord.put(9, parseCoordString(tmpParsedString));
        }

        linea.setpCoord(tmpPcoord);
        linea.setfCoord(tmpFcoord);

    }

    /**
     *
     * @param coordString
     * @return
     */
    private LinkedList<LatLng> parseCoordString(String coordString){
        LinkedList<LatLng> coordSet = new LinkedList();

        //coord String has this pattern: lat1,long1;lat2,long2;....;latN,longN
        StringTokenizer stringTokenized = new StringTokenizer(coordString, ";");

        /*Iterating every couple of coordinates*/
        while(stringTokenized.hasMoreTokens()){


            String tmpSubString = stringTokenized.nextToken();

            //tmpSubString has this pattern: lat,long
            StringTokenizer tmpSubToken = new StringTokenizer(tmpSubString, ",");

            double latitude = Double.parseDouble(tmpSubToken.nextToken());
            double longitude = Double.parseDouble(tmpSubToken.nextToken());

            LatLng coord = new LatLng(latitude, longitude);
            coordSet.add(coord);
        }

        return  coordSet;
    }

    /**
     *
     *
     * @param linea
     * @return
     */
    public boolean setUpBusStop(LineaDescriptor linea, LinkedList<FermataDescriptor> busStop){
        if(linea.getRoute().isEmpty()){
            linea.setBusStops(busStop);
            return false;
        } else {
            linea.setBusStops(busStopOrderer(busStop, linea.getpCoord()));
            linea.setReadyToBePrinted(true);
            return true;
        }
    }


    /**
     * Create a route, ad if busStop are alredy present, order the busStops and return true, else
     * return false
     *
     * @param linea
     * @return
     */
    public boolean setUpRoute(LineaDescriptor linea){
        HashMap<Integer, LinkedList<LatLng>> pCoord = linea.getpCoord();
        LinkedList<LatLng> targetRoute = new LinkedList();

        //creating a route
        int nLine = pCoord.size();

        for(int i = 0; i < nLine; i++){
            LinkedList<LatLng> value = pCoord.get(i);
            targetRoute.addAll(value);
        }

        linea.setRoute(targetRoute);

        //order the bus Stop if are present
        LinkedList<FermataDescriptor> busStop = linea.getBusStops();

        if(busStop != null && !busStop.isEmpty()){
            LinkedList<FermataDescriptor> busStopOrdered = busStopOrderer(busStop, pCoord);
            linea.setBusStops(busStopOrdered);
            linea.setReadyToBePrinted(true);
            return true;
        }

        return false;
    }

    /**
     * Return a list of busStop ordered according to pCoord route
     * @param busStops
     * @param pCoord
     * @return
     */
    private LinkedList<FermataDescriptor> busStopOrderer(LinkedList<FermataDescriptor> busStops, HashMap<Integer, LinkedList<LatLng>> pCoord){
        LinkedList<FermataDescriptor> busStopNotOrdered = busStops;
        LinkedList<FermataDescriptor> busStopOrdered = new LinkedList();

        int pSize = pCoord.size();
        for(int i = 0; i<pSize; i++){
            LinkedList<LatLng> partialRoute = pCoord.get(i);
            ListIterator<LatLng> it = partialRoute.listIterator();
            while(it.hasNext()){
                LatLng tmpCoord = it.next();

                ListIterator<FermataDescriptor> fermateIt = busStopNotOrdered.listIterator();

                while(fermateIt.hasNext()){
                    FermataDescriptor tmp = fermateIt.next();
                    if(match(tmp.getCoordinates(), tmpCoord)){
                        busStopOrdered.addLast(tmp);
                        fermateIt.remove();
                    }
                }
            }

            if(busStopNotOrdered.isEmpty()){
                return busStopOrdered;
            }
        }

        return busStopOrdered;
    }

    /**
     *
     * @param coord1
     * @param coord2
     * @return
     */
    private boolean match(LatLng coord1, LatLng coord2){
        String latitude1 = Double.toString(coord1.latitude).substring(0, 6);
        String latitude2 = Double.toString(coord2.latitude).substring(0, 6);
        if(latitude1.compareTo(latitude2) == 0){
            String longitude1 = Double.toString(coord1.longitude).substring(0, 6);
            String longitude2 = Double.toString(coord2.longitude).substring(0, 6);
            if(longitude1.compareTo(longitude2) == 0){
                return true;
            }

        }
        return false;
    }

    /**
     *
     * @param busPositionJSON
     * @return
     */
    public LinkedList<BusDescriptor> parseBusPositionJSON(String busPositionJSON){
        LinkedList<BusDescriptor> busList = new LinkedList();
        
        return busList;
    }

}
