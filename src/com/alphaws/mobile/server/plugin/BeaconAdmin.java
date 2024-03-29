/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphaws.mobile.server.plugin;

import com.alphaws.mobile.server.common.Beacon;
import com.alphaws.mobile.server.common.BeaconPublicity;
import com.alphaws.mobile.server.common.Branch;
import com.alphaws.mobile.server.common.Campaign;
import com.alphaws.mobile.server.common.Company;
import com.alphaws.mobile.server.common.Content;
import com.alphaws.mobile.server.common.Location;
import com.alphaws.mobile.server.common.Relation;
import com.alphaws.mobile.server.common.ResponseBean;
import com.alphaws.mobile.server.common.User;
import com.alphaws.mobile.server.logger.ServerLogger;
import com.alphaws.mobile.server.protocol.Package;
import com.alphaws.mobile.server.protocol.ProtocolData;
import com.alphaws.mobile.server.protocol.Task;
import com.alphaws.mobile.server.util.ConfigLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Mac;

/**
 *
 * @author patrick
 */
public class BeaconAdmin extends PluginHelperClass {

    private static String pluginType = "8888";
    private String pluginName = "8888";
    private String workerThreadName = "";
    private Task pluginRequestTask = null;
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private ConfigLoader conf = null;
    protected Connection con = null;
    protected String strError = null;
    private long latencyEnd;
    private CallableStatement cs;
    private boolean lValid = true;
    private String error;
    private ResponseBean response = new ResponseBean();
    private String api_key;
    private Package oPkgResponse;
    private ProtocolData oDataResponse;

    /*
    *-------*
    |       |
    |       |   
    |   x   |
    |       |
    *-------*
     */
    private static final double DIAG_DIST = 15.0d;

    public BeaconAdmin() {
        super();
        conf = getConfigLoader();
        ServerLogger.getLogger().log("BeaconAdmin", "Created new Instance...");
    }

    @Override
    public String getWorkerThreadName() {
        return workerThreadName;
    }

    @Override
    public void setWorkerThreadName(String workerThreadName) {
        this.workerThreadName = workerThreadName;
    }

    @Override
    public Object plugInRequest(Task task) {
        return pluginRequestTask = task;
    }

    @Override
    public void setPluginType(String cType) {
        pluginType = cType;
    }

    @Override
    public String getPluginType() {
        return pluginType;
    }

    @Override
    public Boolean start() {
        return true;
    }

    @Override
    public void setPluginName(String cName) {
        pluginName = cName;
    }

    @Override
    public String getPluginName() {
        return pluginName;
    }

    @Override
    public synchronized void update(Observable o, Object arg) {
        super.update(o, arg);
    }

    @Override
    public Task call() {
        //start taking the time for the latency
        long latencyStart = System.currentTimeMillis();

        Boolean lResult = Boolean.FALSE;
        Package oPkg = pluginRequestTask.getTaskData();
        ProtocolData oData = oPkg.getData();

        oPkgResponse = new Package();
        oDataResponse = oPkgResponse.getData();

        Map<Object, Object> map = (Map<Object, Object>) oData.getRawData();

        ServerLogger.getLogger().log("BeaconAdmin", "Entered plugin BeaconAdmin....");

        try {
            con = getDBConnection("BEACONADMIN");
            if (con == null || con.isClosed()) {
                ServerLogger.getLogger().log("BeaconAdmin", "No DB connection....");
                response.setErrorCode(-1);
                response.setErrorMessage("MISSING DATABASE CONNECTION");
                response.setResponse("Error");
                response.setKnownBean(-1);
                response.setLatency(System.currentTimeMillis() - latencyStart);
                oDataResponse.setRawData(response);
                oPkgResponse.setData(oDataResponse);
                Task taskResponse = new Task();
                taskResponse.setTaskData(oPkgResponse);
                return taskResponse;
            }

            Integer nVal = Integer.valueOf((String) map.get("action"));
            Boolean isKeyValid = false;
            Integer cid = null;

            if (nVal == 1 || map.containsKey("api-key")) {
                if (nVal != 1) {
                    api_key = (String) map.get("api-key");
                    if (map.containsKey("cid")) {
                        cid = Integer.valueOf((String) map.get("cid"));
                        isKeyValid = checkApiKey(api_key, cid);
                    } else {
                        return generateErrorResponse("THE API KEY INVALID", "API-KEY", -998, null, "API-KEY CANNOT BE IDENTIFIED");
                    }
                } else {
                    isKeyValid = true;
                }
            }

            if (!isKeyValid) {
                return generateErrorResponse("THE API KEY IS MISSING", "API-KEY", -999, null, "NO API-KEY");
            }

            ServerLogger.getLogger().log("BeaconAdmin", map.toString());

            switch (nVal) {

                case 1:

                    User u = loginUser(map);
                    if (u != null) {
                        response.setKnownBean(u);
                    } else {
                        Task t = generateErrorResponse("User invalid", "Authentication", -10, null, "invalid");
                        return t;
                    }
                    break;

                case 2:
                    response.setKnownBean(getCampaigns(map));
                    break;

                case 3:
                    response.setKnownBean(getBranches(map));
                    break;

                case 4:
                    response.setKnownBean(getBeacons(map));
                    break;

                case 5:
                    response.setKnownBean(saveCampaign(map));
                    break;

                case 6:
                    response.setKnownBean(saveLocation(map));
                    break;

                case 7:
                    response.setKnownBean(assignContent(map));
                    break;

                case 8:
                    response.setKnownBean(updateBeaconInformation(map));
                    break;

                case 9:
                    response.setKnownBean(getAllRelations(map));
                    break;

                case 10:
                    response.setKnownBean(beaconRangeChange(map));
                    break;

                case 11:
                    response.setKnownBean(getBeaconsByLocation(map));
                    break;

                case 12:
                    response.setKnownBean(deleteContentAssignment(map));
                    break;
            }

        } catch (SQLException ex) {
            Logger.getLogger(BeaconAdmin.class.getName()).log(Level.SEVERE, null, ex);

            ServerLogger.getLogger().log("BeaconAdmin", "DB ERROR....");
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(BeaconAdmin.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            response.setErrorMessage("DATABASE ERROR");
            response.setResponse("DATABASE");
            response.setErrorCode(-2);
            response.setToken(null);
            response.setKnownBean(ex.getMessage());
            response.setLatency(System.currentTimeMillis() - latencyStart);
            oDataResponse.setRawData(response);
            oPkgResponse.setData(oDataResponse);
            Task taskResponse = new Task();
            taskResponse.setTaskData(oPkgResponse);
            return taskResponse;
        } catch (NumberFormatException ex) {
            ServerLogger.getLogger().log("BeaconAdmin NumberFormatException", ex.getMessage());
        } finally {

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(BeaconAdmin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        response.setErrorMessage("success");
        response.setResponse("BEACON_ADMIN");
        response.setErrorCode(0);
        response.setToken(null);
        response.setLatency(System.currentTimeMillis() - latencyStart);
        oDataResponse.setRawData(response);
        oPkgResponse.setData(oDataResponse);
        Task taskResponse = new Task();
        taskResponse.setTaskData(oPkgResponse);

        if (con != null) {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(BeaconAdmin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        response = null;

        return taskResponse;

    }

    private Task generateErrorResponse(String error, String responseMessage, int errorCode, String token, String infoBean) {
        response.setErrorMessage(error);
        response.setResponse(responseMessage);
        response.setErrorCode(errorCode);
        response.setToken(token);
        response.setKnownBean(infoBean);
        oDataResponse.setRawData(response);
        oPkgResponse.setData(oDataResponse);
        Task taskResponse = new Task();
        taskResponse.setTaskData(oPkgResponse);
        return taskResponse;
    }

    protected User loginUser(Map<Object, Object> map) {
        User u = null;

        try {

            if (cs != null) {
                cs.clearBatch();
                cs.clearParameters();
                cs.clearWarnings();
            }

            String user = null;
            String pwd = null;

            if (map.containsKey("user") && map.containsKey("pwd")) {
                user = (String) map.get("user");
                pwd = (String) map.get("pwd");
            }

            cs = con.prepareCall("{call login(?,?)}");

            if (user != null && pwd != null) {
                cs.setString(1, user.trim());
                cs.setString(2, pwd.trim());
            }

            if (cs.execute() && lValid) {
                ResultSet rs = cs.getResultSet();
                if (rs.next()) {

                    Company cmpy = new Company(rs.getInt("id"), rs.getString("name"), rs.getString("direction"), rs.getString("logo"),
                            rs.getString("color"), rs.getString("email_contact"), null, null, null, rs.getString("apikey"));

                    u = new User(rs.getInt("userid"), rs.getString("name"), rs.getInt("profile"));
                    u.setCompany(cmpy);
                }
            }

            cs.close();
        } catch (SQLException ex) {
            ServerLogger.getLogger().log("BeaconAdmin", "Beacons: " + ex.getSQLState() + "|" + ex.getMessage());
        } catch (NumberFormatException ex) {
            ServerLogger.getLogger().log("BeaconAdmin Number Exception", "Beacons: " + ex.getLocalizedMessage());
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(BeaconAdmin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return u;

    }

    private Object getCampaigns(Map<Object, Object> map) {
        ArrayList<Campaign> cpList = new ArrayList<Campaign>();

        try {

            if (cs != null) {
                cs.clearBatch();
                cs.clearParameters();
                cs.clearWarnings();
            }

            Integer cid = null;

            if (map.containsKey("cid")) {
                cid = Integer.parseInt((String) map.get("cid"));
            }

            cs = con.prepareCall("{call getCampaignsByCompany(?)}");

            if (cid != null) {
                cs.setInt(1, cid);
            }

            if (cs.execute() && lValid) {
                ResultSet rs = cs.getResultSet();
                while (rs.next()) {

                    Campaign cmp = new Campaign(rs.getInt("id"), rs.getInt("id_company"), rs.getString("name"), rs.getString("notification"),
                            rs.getString("image"), rs.getString("detail"), rs.getString("short_url"), rs.getString("create_date"), rs.getString("update_date"));
                    cpList.add(cmp);
                }
            }

            //cs.close();
        } catch (SQLException ex) {
            ServerLogger.getLogger().log("BeaconAdmin", "Beacons: " + ex.getSQLState() + "|" + ex.getMessage());
        } catch (NumberFormatException ex) {
            ServerLogger.getLogger().log("BeaconAdmin Number Exception", "Beacons: " + ex.getLocalizedMessage());
        }
        
        /*
        finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(BeaconAdmin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
*/
        return cpList;

    }

    private Object getBranches(Map<Object, Object> map) {
        ArrayList<Branch> branchList = new ArrayList<Branch>();

        try {

            if (cs != null) {
                cs.clearBatch();
                cs.clearParameters();
                cs.clearWarnings();
            }

            Integer cid = null;

            if (map.containsKey("cid")) {
                cid = Integer.parseInt((String) map.get("cid"));
            }

            cs = con.prepareCall("{call getLocationsByCompany(?)}");

            if (cid != null) {
                cs.setInt(1, cid);
            }

            if (cs.execute() && lValid) {
                ResultSet rs = cs.getResultSet();
                while (rs.next()) {

                    Branch branch = new Branch(rs.getInt("id"), rs.getInt("id_company"), rs.getString("name"), rs.getString("direction"),
                            rs.getInt("lat"), rs.getInt("lng"), rs.getString("update_date"));
                    branchList.add(branch);
                }
            }

            //cs.close();
        } catch (SQLException ex) {
            ServerLogger.getLogger().log("BeaconAdmin", "Beacons: " + ex.getSQLState() + "|" + ex.getMessage());
        } catch (NumberFormatException ex) {
            ServerLogger.getLogger().log("BeaconAdmin Number Exception", "Beacons: " + ex.getLocalizedMessage());
        }
        
        /*
        finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(BeaconAdmin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        */
        return branchList;
    }

    private Object getBeacons(Map<Object, Object> map) {
        ArrayList<Beacon> beaconList = new ArrayList<Beacon>();

        try {

            if (cs != null) {
                cs.clearBatch();
                cs.clearParameters();
                cs.clearWarnings();
            }

            Integer cid = null;

            if (map.containsKey("cid")) {
                cid = Integer.parseInt((String) map.get("cid"));
            }

            cs = con.prepareCall("{call getBeaconByCompany(?)}");

            if (cid != null) {
                cs.setInt(1, cid);
            }

            if (cs.execute() && lValid) {
                ResultSet rs = cs.getResultSet();
                while (rs.next()) {
                    beaconList.add(new Beacon(rs.getInt("id"), rs.getInt("id_company"), rs.getInt("id_location"), rs.getString("code"),
                            rs.getString("name"), rs.getInt("type"), rs.getString("uuid"), rs.getInt("major"), rs.getInt("minor"), rs.getString("tx"),
                            rs.getString("version"), null, rs.getInt("battery")));
                }
            }

           // cs.close();
        } catch (SQLException ex) {
            ServerLogger.getLogger().log("BeaconAdmin", "Beacons: " + ex.getSQLState() + "|" + ex.getMessage());
        } catch (NumberFormatException ex) {
            ServerLogger.getLogger().log("BeaconAdmin Number Exception", "Beacons: " + ex.getLocalizedMessage());
        } 
        /*
        finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(BeaconAdmin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
*/
        return beaconList;
    }

    /**
     * id=8888&type=8888&action=5&stamp=BeaconIOAdmin&cid=1&name=Foo&notify=Bar&detail=Das
     * ist ein Test cid= Comapny ID name=Campaign name notify=Notification text
     * img=Image name detail=Detailed message url=short URL
     *
     * @param map
     * @return
     */
    private Object saveCampaign(Map<Object, Object> map) {
        int result = 0;
        lValid = true;
        try {

            if (cs != null) {
                cs.clearBatch();
                cs.clearParameters();
                cs.clearWarnings();
            }

            Integer cid = null;
            String name = null;
            String notify = null;
            String img = null;
            String detail = null;
            String shortUrl = null;
            Integer user = null;

            if (map.containsKey("cid")) {
                cid = Integer.parseInt((String) map.get("cid"));
            }

            if (map.containsKey("name")) {
                name = (String) map.get("name");
            }

            if (map.containsKey("notify")) {
                notify = (String) map.get("notify");
            }

            if (map.containsKey("img")) {
                img = (String) map.get("img");
            }

            if (map.containsKey("detail")) {
                detail = (String) map.get("detail");
            }

            if (map.containsKey("url")) {
                shortUrl = (String) map.get("url");
            }

            if (map.containsKey("user")) {
                user = Integer.parseInt((String) map.get("user"));
            }

            cs = con.prepareCall("{call saveCampaign(?,?,?,?,?,?,?,?)}");

            if (cid != null) {
                cs.setInt(1, cid);
            } else {
                lValid = false;
                error = "Company ID is missing";
            }

            if (name != null) {
                cs.setString(2, name);
            } else {
                lValid = false;
                error = "Campaign Name is missing";
            }

            if (notify != null) {
                cs.setString(3, notify);
            } else {
                lValid = false;
                error = "Notification Message is missing";
            }

            if (img != null) {
                cs.setString(4, img);
            } else {
                cs.setNull(4, java.sql.Types.NULL);
            }

            if (detail != null) {
                cs.setString(5, detail);
            } else {
                lValid = false;
                error = "Campaign Message is missing";
            }

            if (shortUrl != null) {
                cs.setString(6, shortUrl);
            } else {
                cs.setNull(6, java.sql.Types.NULL);
            }

            if (user != null) {
                cs.setInt(7, user);
            } else {
                lValid = false;
                error = "User ID is missing";
            }

            cs.registerOutParameter(8, java.sql.Types.INTEGER);

            if (lValid) {
                cs.executeUpdate();
                ServerLogger.getLogger().log(getClass().getCanonicalName(), cs.toString());
                result = cs.getInt(8);
            } else {
                ServerLogger.getLogger().log(getClass().getCanonicalName(), "ERROR WHILE MAPPING PARAMETERS: " + error);
            }

            ServerLogger.getLogger().log("BeaconAdmin", "RESULTSET OF THE CAMPAIGN:" + result + "");

        } catch (SQLException ex) {
            ServerLogger.getLogger().log("BeaconAdmin", "Beacons: " + ex.getSQLState() + "|" + ex.getMessage());
        } catch (NumberFormatException ex) {
            ServerLogger.getLogger().log("BeaconAdmin Number Exception", "Beacons: " + ex.getLocalizedMessage());
        } finally {
            try {
                cs.close();
                con.close();
                ServerLogger.getLogger().log("BeaconAdmin", "DB CONNECTION CLOSED");
                cs = null;
                con = null;
            } catch (SQLException ex) {
                Logger.getLogger(BeaconAdmin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return result;

    }

    private Object saveLocation(Map<Object, Object> map) {
        int result = 0;
        lValid = true;
        try {

            if (cs != null) {
                cs.clearBatch();
                cs.clearParameters();
                cs.clearWarnings();
            }

            Integer cid = null;
            String name = null;
            String dir = null;
            Integer user = null;
            Integer lat = null;
            Integer lng = null;

            if (map.containsKey("cid")) {
                cid = Integer.parseInt((String) map.get("cid"));
            }

            if (map.containsKey("lat")) {
                lat = Integer.parseInt((String) map.get("lat"));
            }

            if (map.containsKey("lng")) {
                lng = Integer.parseInt((String) map.get("lng"));
            }

            if (map.containsKey("name")) {
                name = (String) map.get("name");
            }

            if (map.containsKey("dir")) {
                dir = (String) map.get("dir");
            }

            if (map.containsKey("user")) {
                user = Integer.parseInt((String) map.get("user"));
            }

            cs = con.prepareCall("{call saveLocation(?,?,?,?,?,?,?)}");

            if (cid != null) {
                cs.setInt(1, cid);
            } else {
                lValid = false;
                error = "Company ID is missing";
            }

            if (name != null) {
                cs.setString(2, name);
            } else {
                lValid = false;
                error = "Location Name is missing";
            }

            if (dir != null) {
                cs.setString(3, dir);
            } else {
                lValid = false;
                error = "Location direction is missing";
            }

            if (user != null) {
                cs.setInt(4, user);
            } else {
                lValid = false;
                error = "User ID is missing";
            }

            if (lat != null) {
                cs.setInt(5, lat);
            } else {
                lValid = false;
                error = "Latitud is missing";
            }

            if (lng != null) {
                cs.setInt(6, lng);
            } else {
                lValid = false;
                error = "Longitud is missing";
            }

            cs.registerOutParameter(7, java.sql.Types.INTEGER);

            if (lValid) {
                cs.executeUpdate();
                ServerLogger.getLogger().log(getClass().getCanonicalName(), cs.toString());
                result = cs.getInt(7);
            } else {
                ServerLogger.getLogger().log(getClass().getCanonicalName(), "ERROR WHILE MAPPING PARAMETERS: " + error);
            }

            ServerLogger.getLogger().log("BeaconAdmin", "RESULTSET OF THE LOCATION:" + result + "");

        } catch (SQLException ex) {
            ServerLogger.getLogger().log("BeaconAdmin", "Beacons: " + ex.getSQLState() + "|" + ex.getMessage());
        } catch (NumberFormatException ex) {
            ServerLogger.getLogger().log("BeaconAdmin Number Exception", "Beacons: " + ex.getLocalizedMessage());
        } finally {
            try {
                cs.close();
                con.close();
                ServerLogger.getLogger().log("BeaconAdmin", "DB CONNECTION CLOSED");
                cs = null;
                con = null;
            } catch (SQLException ex) {
                Logger.getLogger(BeaconAdmin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return result;

    }

    private Object assignContent(Map<Object, Object> map) {
        int result = 0;
        lValid = true;
        try {

            if (cs != null) {
                cs.clearBatch();
                cs.clearParameters();
                cs.clearWarnings();
            }

            Integer cid = null;
            Integer cmp = null;
            Integer bcon = null;
            Integer user = null;
            Integer dist = null;
            Long start = null;
            Long end = null;

            if (map.containsKey("cid")) {
                cid = Integer.parseInt((String) map.get("cid"));
            }

            if (map.containsKey("cmp")) {
                cmp = Integer.parseInt((String) map.get("cmp"));
            }

            if (map.containsKey("bcon")) {
                bcon = Integer.parseInt((String) map.get("bcon"));
            }

            if (map.containsKey("user")) {
                user = Integer.parseInt((String) map.get("user"));
            }

            if (map.containsKey("dist")) {
                dist = Integer.parseInt((String) map.get("dist"));
            }

            if (map.containsKey("start")) {
                start = Long.parseLong((String) map.get("start"));
            }

            if (map.containsKey("end")) {
                end = Long.parseLong((String) map.get("end"));
            }

            cs = con.prepareCall("{call assignContent(?,?,?,?,?,?,?,?)}");

            if (cid != null) {
                cs.setInt(1, cid);
            } else {
                lValid = false;
                error = "Company ID is missing";
            }

            if (cmp != null) {
                cs.setInt(2, cmp);
            } else {
                lValid = false;
                error = "Campaign ID is missing";
            }

            if (bcon != null) {
                cs.setInt(3, bcon);
            } else {
                lValid = false;
                error = "Beacon ID is missing";
            }

            if (dist != null) {
                cs.setInt(4, dist);
            } else {
                lValid = false;
                error = "Beacon Range is missing";
            }

            if (start != null) {
                cs.setTimestamp(5, new Timestamp(start));
            } else {
                lValid = false;
                error = "Start date is missing";
            }

            if (end != null) {
                cs.setTimestamp(6, new Timestamp(end));
            } else {
                lValid = false;
                error = "End date is missing";
            }

            if (user != null) {
                cs.setInt(7, user);
            } else {
                lValid = false;
                error = "User ID is missing";
            }

            cs.registerOutParameter(8, java.sql.Types.INTEGER);

            if (lValid) {
                cs.executeUpdate();
                ServerLogger.getLogger().log(getClass().getCanonicalName(), cs.toString());
                result = cs.getInt(8);
            } else {
                ServerLogger.getLogger().log(getClass().getCanonicalName(), "ERROR WHILE MAPPING PARAMETERS: " + error);
            }

            ServerLogger.getLogger().log("BeaconAdmin", "RESULTSET OF THE LOCATION:" + result + "");

        } catch (SQLException ex) {
            ServerLogger.getLogger().log("BeaconAdmin", "Beacons: " + ex.getSQLState() + "|" + ex.getMessage());
        } catch (NumberFormatException ex) {
            ServerLogger.getLogger().log("BeaconAdmin Number Exception", "Beacons: " + ex.getLocalizedMessage());
        } finally {
            try {
                cs.close();
                con.close();
                ServerLogger.getLogger().log("BeaconAdmin", "DB CONNECTION CLOSED");
                cs = null;
                con = null;
            } catch (SQLException ex) {
                Logger.getLogger(BeaconAdmin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return result;

    }

    /**
     * Update Battery Life of the Beacon battery
     *
     * @param map
     * @return
     */
    private Object updateBeaconInformation(Map<Object, Object> map) {

        int battery = -1;
        int id = -1;
        String uuid = "";
        int major = 0;
        int minor = 0;

        if (map.containsKey("bcon_battery")) {
            battery = Integer.valueOf((String) map.get("bcon_battery"));
        }

        if (map.containsKey("bcon_uuid")) {
            uuid = (String) map.get("bcon_uuid");
        }

        if (map.containsKey("bcon_major")) {
            major = Integer.valueOf((String) map.get("bcon_major"));
        }

        if (map.containsKey("bcon_minor")) {
            minor = Integer.valueOf((String) map.get("bcon_minor"));
        }

        if (!uuid.isEmpty() && major > 0 && minor > 0 && battery > 0) {
            ServerLogger.getLogger().log("BeaconAdmin", "Beacon Battery Update: " + battery + " / " + uuid + " / " + major + " / " + minor);

            try {
                if (cs != null) {
                    cs.clearBatch();
                    cs.clearParameters();
                    cs.clearWarnings();
                }

                cs = con.prepareCall("{call updateBeaconInfo(?,?,?,?)}");

                cs.setInt(1, battery);
                cs.setString(2, uuid);
                cs.setInt(3, major);
                cs.setInt(4, minor);

                cs.executeUpdate();

                ServerLogger.getLogger().log("BeaconAdmin", "Beacon Update successfull");

                cs.close();
            } catch (SQLException ex) {
                ServerLogger.getLogger().log("BeaconAdmin", ex.getLocalizedMessage());
            } finally {
                if (cs != null) {
                    try {
                        cs.close();
                        con.close();
                    } catch (SQLException ex) {
                        ServerLogger.getLogger().log("BeaconAdmin", ex.getLocalizedMessage());
                    }
                }

                cs = null;
                con = null;
            }

        }

        return 1;
    }

    private Boolean checkApiKey(String api_key, int id) {
        Boolean result = false;

        try {
            if (cs != null) {
                cs.clearBatch();
                cs.clearParameters();
                cs.clearWarnings();
            }

            cs = con.prepareCall("{call checkApiKey(?,?)}");
            cs.setString(1, api_key.trim());
            cs.setInt(2, id);
            ServerLogger.getLogger().log("BeaconAdmin", "Checking API-Key");

            ServerLogger.getLogger().log("BeaconAdmin", api_key + " / " + id);

            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                ServerLogger.getLogger().log("BeaconAdmin", "API-Key valid");
                ServerLogger.getLogger().log("BeaconAdmin", "API-Key Result:" + rs.getInt("API"));
                result = (rs.getInt("API") == 0 ? false : true);
            }

        } catch (SQLException ex) {
            ServerLogger.getLogger().log("BeaconAdmin", ex.getLocalizedMessage());
        } finally {
            try {
                cs.clearBatch();
                cs.clearParameters();
                cs.clearWarnings();
            } catch (SQLException ex) {
                Logger.getLogger(BeaconAdmin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return result;

    }

    /**
     *
     * @param lat
     * @param lng
     * @param bearing
     * @param d
     * @param pos
     */
    private void getLocation(double lat, double lng, double bearing, double d, double[] pos) {
        double dist = d / 6371;
        double brng = Math.toRadians(bearing);
        double lat1 = Math.toRadians(lat);
        double lon1 = Math.toRadians(lng);

        double lat2 = Math.asin(Math.sin(lat1) * Math.cos(dist) + Math.cos(lat1) * Math.sin(dist) * Math.cos(brng));
        double a = Math.atan2(Math.sin(brng) * Math.sin(dist) * Math.cos(lat1), Math.cos(dist) - Math.sin(lat1) * Math.sin(lat2));
        System.out.println("a = " + a);
        double lon2 = lon1 + a;

        lon2 = (lon2 + 3 * Math.PI) % (2 * Math.PI) - Math.PI;

        //System.out.println("Latitude = "+Math.toDegrees(lat2)+"\nLongitude = "+Math.toDegrees(lon2));
        pos[0] = Math.toDegrees(lat2);
        pos[1] = Math.toDegrees(lon2);

    }

    private ArrayList<Location> getNearbyLocations(double lat, double lng) {
        ArrayList<Location> locList = new ArrayList<>();

        try {
            if (cs != null) {
                cs.clearBatch();
                cs.clearParameters();
                cs.clearWarnings();
            }

            cs = con.prepareCall("{call getNearbyLocations(?,?,?,?,?,?,?,?)}");
            int k = 45;
            int counter = 1;
            double[] pos = new double[2];
            for (int i = 0; i < 3; i++) {
                getLocation(lat, lng, k, DIAG_DIST, pos);
                cs.setDouble(counter, pos[0]);
                cs.setDouble(counter + 1, pos[1]);
                ServerLogger.getLogger().log("BeaconAdmin", pos[0] + " / " + pos[1]);
            }

            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                locList.add(new Location(rs.getInt("id_company"), rs.getDouble("lat"), rs.getDouble("lng"), rs.getString("name"), rs.getString("direction")));
            }

        } catch (SQLException ex) {
            ServerLogger.getLogger().log("BeaconAdmin", ex.getLocalizedMessage());
        } finally {
            try {
                cs.clearBatch();
                cs.clearParameters();
                cs.clearWarnings();
            } catch (SQLException ex) {
                Logger.getLogger(BeaconAdmin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return locList;
    }

    private Object getAllRelations(Map<Object, Object> map) {

        ArrayList<Relation> relList = new ArrayList<>();
        ArrayList<Branch> locationList = new ArrayList<>();

        HashMap<Branch, HashMap<Beacon, HashMap<Integer, ArrayList<Campaign>>>> results = new HashMap<>();

        try {

            if (cs != null) {
                cs.clearBatch();
                cs.clearParameters();
                cs.clearWarnings();
            }
            con.clearWarnings();
            int cid = Integer.valueOf((String) map.get("cid"));
            cs = con.prepareCall("{call getAssignedRelations(?)}");
            cs.setInt(1, cid);

            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                //locList.add(new Location(rs.getInt("id_company"),rs.getDouble("lat"), rs.getDouble("lng"),rs.getString("name"),rs.getString("direction")));
                relList.add(new Relation(rs.getInt("id"),rs.getInt("id_company"), rs.getInt("id_campaign"), rs.getInt("id_beacon"), rs.getInt("dist"),
                        rs.getString("start_date"), rs.getString("end_date")));
            }

            cs.clearBatch();
            cs.clearParameters();
            cs.clearWarnings();

            if (relList.isEmpty()) {
                return null;
            }

            resetConnection();
            con.clearWarnings();
            
            ArrayList<Branch> branches = (ArrayList<Branch>) getBranches(map);
            HashMap<Integer, Branch> mapBranches = new HashMap();
            for (Iterator<Branch> iterator = branches.iterator(); iterator.hasNext();) {
                Branch next = iterator.next();
                mapBranches.put(next.getId(), next);
            }

            resetConnection();
            con.clearWarnings();
            
            ArrayList<Campaign> campaigns = (ArrayList<Campaign>) getCampaigns(map);
            HashMap<Integer, Campaign> mapCampaigns = new HashMap();
            for (Campaign campaign : campaigns) {
                mapCampaigns.put(campaign.getId(), campaign);
            }

            resetConnection();
            con.clearWarnings();
            
            ArrayList<Beacon> beacons = (ArrayList<Beacon>) getBeacons(map);
            HashMap<Integer, Beacon> mapBeacons = new HashMap();
            for (Beacon beacon : beacons) {
                mapBeacons.put(beacon.getId(), beacon);
            }

            resetConnection();

            HashMap<Beacon, HashMap<Integer, ArrayList<Campaign>>> bconcmp = new HashMap<>();
            ArrayList<Campaign> cmps = new ArrayList<>();

            HashMap<Integer, ArrayList<Campaign>> distList = new HashMap<>();

            ArrayList<Branch> retBr = new ArrayList<>();
            ServerLogger.getLogger().log("BeaconAdmin", (new Gson()).toJson(relList));
            Campaign c = null;
            for (Beacon beacon : beacons) {
                for (Relation relation : relList) {
                    if (relation.getId_beacon() == beacon.getId()) {
                        c = mapCampaigns.get(relation.getId_campaign());
                        if(c != null){
                            ServerLogger.getLogger().log("BeaconAdmin", ""+relation.getDist());
                            c.setStart_date(relation.getStart_date());
                            c.setEnd_date(relation.getEnd_date());
                            c.setRelationsID(relation.getId());
                            switch (relation.getDist()) {
                                case 1:
                                    beacon.addShortCampaign(c);
                                    break;
                                case 2:
                                    beacon.addMediumCampaign(c);
                                    break;
                                case 3:
                                    beacon.addLargeCampaign(c);
                                    break;
                            }
                        }
                    }
                    c = null;
                    relation = null;
                }
                
                Branch b = mapBranches.get(beacon.getId_location());
                b.addBeacon(beacon);
                mapBranches.replace(beacon.getId_location(), b);
                b = null;
            }

            Content cont = new Content(mapBranches.values());
            
            
            ServerLogger.getLogger().log("BeaconAdmin", (new Gson()).toJson(cont));

            
            return cont;
        

        } catch (SQLException ex) {
            ServerLogger.getLogger().log("BeaconAdmin", ex.getLocalizedMessage());
        } catch (Exception ex){
            ServerLogger.getLogger().log("BeaconAdmin", ex.getMessage());        
        }
        finally {
            try {
                cs.clearBatch();
                cs.clearParameters();
                cs.clearWarnings();
                
            } catch (SQLException ex) {
                Logger.getLogger(BeaconAdmin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        return null;

    }

    private void resetConnection() {
        try {
            if (con == null || con.isClosed()) {
                ServerLogger.getLogger().log("BeaconAdmin", "reseting DB connection");
                con = getDBConnection("BEACONADMIN");
            }
        } catch (SQLException ex) {
            Logger.getLogger(BeaconAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Boolean beaconRangeChange(Map<Object, Object> map) {

        ArrayList<Location> locList = new ArrayList<>();

        try {
            if (cs != null) {
                cs.clearBatch();
                cs.clearParameters();
                cs.clearWarnings();
            }

            cs = con.prepareCall("{call updateDistConfig(?,?,?,?)}");

            int cid = Integer.valueOf((String) map.get("cid"));
            int id = Integer.valueOf((String) map.get("id"));
            int dist = Integer.valueOf((String) map.get("dist"));
            int act = Integer.valueOf((String) map.get("act"));

            cs.setInt(1, id);
            cs.setInt(2, dist);
            cs.setInt(3, cid);
            cs.setInt(4, act);

            int ret = cs.executeUpdate();

            if (ret > 0) {
                return true;
            }

        } catch (SQLException ex) {
            ServerLogger.getLogger().log("BeaconAdmin", ex.getLocalizedMessage());
        } finally {
            try {
                cs.clearBatch();
                cs.clearParameters();
                cs.clearWarnings();
            } catch (SQLException ex) {
                Logger.getLogger(BeaconAdmin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return false;
    }

    private Object getBeaconsByLocation(Map<Object, Object> map) {

        ArrayList<Beacon> beaconList = new ArrayList<Beacon>();

        try {

            if (cs != null) {
                cs.clearBatch();
                cs.clearParameters();
                cs.clearWarnings();
            }

            Integer loc = null;
            Integer cid = null;

            cid = Integer.parseInt((String) map.get("cid"));

            if (map.containsKey("loc")) {
                loc = Integer.parseInt((String) map.get("loc"));
            }

            cs = con.prepareCall("{call getBeaconByLocation(?,?)}");

            if (cid != null) {
                cs.setInt(1, cid);
            }

            if (loc != null) {
                cs.setInt(2, loc);
            }

            if (cs.execute() && lValid) {
                ResultSet rs = cs.getResultSet();
                while (rs.next()) {
                    beaconList.add(new Beacon(rs.getInt("id"), rs.getInt("id_company"), rs.getInt("id_location"), rs.getString("code"),
                            rs.getString("name"), rs.getInt("type"), rs.getString("uuid"), rs.getInt("major"), rs.getInt("minor"), rs.getString("tx"),
                            rs.getString("version"), null, rs.getInt("battery")));
                }
            }

            cs.close();
        } catch (SQLException ex) {
            ServerLogger.getLogger().log("BeaconAdmin", "Beacons: " + ex.getSQLState() + "|" + ex.getMessage());
        } catch (NumberFormatException ex) {
            ServerLogger.getLogger().log("BeaconAdmin Number Exception", "Beacons: " + ex.getLocalizedMessage());
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(BeaconAdmin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return beaconList;

    }

    private Object deleteContentAssignment(Map<Object, Object> map) {

         try {
            if (cs != null) {
                cs.clearBatch();
                cs.clearParameters();
                cs.clearWarnings();
            }

            cs = con.prepareCall("{call deleteContentAssignement(?,?)}");
            cs.setInt(1, Integer.parseInt((String) map.get("cid")));
            cs.setInt(2, Integer.parseInt((String) map.get("rid")));
           
            int ret = cs.executeUpdate();

            return true;
        } catch (SQLException ex) {
            ServerLogger.getLogger().log("BeaconAdmin", ex.getLocalizedMessage());
        } finally {
            try {
                cs.clearBatch();
                cs.clearParameters();
                cs.clearWarnings();
            } catch (SQLException ex) {
                Logger.getLogger(BeaconAdmin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

         return false;
    }

}
