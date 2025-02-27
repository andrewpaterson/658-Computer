package net.logicim.domain.common.propagation;

import net.common.SimulatorException;
import net.common.csv.CSVFileReader;
import net.common.csv.CSVRow;
import net.common.util.EnvironmentInspector;
import net.common.util.StringUtil;
import net.logicim.data.family.Family;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static net.logicim.domain.common.LongTime.nanosecondsToTime;

public class FamilyVoltageConfigurationStore
{
  protected static FamilyVoltageConfigurationStore instance;

  protected LinkedHashMap<Family, FamilyVoltageConfiguration> map;

  public FamilyVoltageConfigurationStore()
  {
    map = new LinkedHashMap<>();
  }

  public static FamilyVoltageConfigurationStore getInstance()
  {
    if (instance == null)
    {
      instance = initialiseFamilyVoltageConfigurationStore();
    }
    return instance;
  }

  static FamilyVoltageConfigurationStore initialiseFamilyVoltageConfigurationStore()
  {
    FamilyVoltageConfigurationStore familyVoltageConfigurationStore = new FamilyVoltageConfigurationStore();

    String programDir = EnvironmentInspector.getProgramDir();
    File file = new File(programDir + "/../Documents/Propagation Times (541).csv");
    Map<String, Map<Float, List<VoltagePropagationTimeRow>>> familyPropagationMap = new LinkedHashMap<>();
    if (file.exists())
    {
      familyPropagationMap = readVoltagePropagationMap(file);
    }

    for (String family : familyPropagationMap.keySet())
    {
      Map<Float, List<VoltagePropagationTimeRow>> voltagePropagationMap = familyPropagationMap.get(family);
      String logicLevel = "";
      for (Float vcc : voltagePropagationMap.keySet())
      {
        List<VoltagePropagationTimeRow> rows = voltagePropagationMap.get(vcc);
        VoltageConfiguration voltageConfiguration = createVoltageConfiguration(vcc, rows);

        for (VoltagePropagationTimeRow row : rows)
        {
          if (!StringUtil.isEmptyOrNull(row.logicLevel))
          {
            logicLevel = row.logicLevel;
          }
        }

        familyVoltageConfigurationStore.add(voltageConfiguration, logicLevel, family);
      }
    }
    familyVoltageConfigurationStore.allConfigurationsAdded();
    return familyVoltageConfigurationStore;
  }

  protected void allConfigurationsAdded()
  {
    for (FamilyVoltageConfiguration familyVoltageConfiguration : map.values())
    {
      familyVoltageConfiguration.sort();
    }
  }

  static VoltageConfiguration createVoltageConfiguration(Float vcc, List<VoltagePropagationTimeRow> rows)
  {
    VoltagePropagationTimeRow tPHL = null;
    VoltagePropagationTimeRow tPLH = null;
    VoltagePropagationTimeRow tPZH = null;
    VoltagePropagationTimeRow tPZL = null;
    VoltagePropagationTimeRow tPHZ = null;
    VoltagePropagationTimeRow tPLZ = null;
    Float vih = null;
    Float vil = null;
    Float voh = null;
    Float vol = null;

    for (VoltagePropagationTimeRow row : rows)
    {
      switch (row.delayType)
      {
        case "tPD":
          tPHL = row;
          tPLH = row;
          break;
        case "tPHL":
          tPHL = row;
          break;
        case "tPLH":
          tPLH = row;
          break;
        case "tEN":
          tPZH = row;
          tPZL = row;
          break;
        case "tPZH":
          tPZH = row;
          break;
        case "tPZL":
          tPZL = row;
          break;
        case "tDIS":
          tPHZ = row;
          tPLZ = row;
          break;
        case "tPHZ":
          tPHZ = row;
          break;
        case "tPLZ":
          tPLZ = row;
          break;
      }

      if (row.vih != null)
      {
        vih = row.vih;
      }
      if (row.vil != null)
      {
        vil = row.vil;
      }
      if (row.voh != null)
      {
        voh = row.voh;
      }
      if (row.vol != null)
      {
        vol = row.vol;
      }
    }

    if (tPHL == null || tPLH == null ||
        tPZH == null || tPZL == null ||
        tPHZ == null || tPLZ == null)
    {
      throw new SimulatorException("tPHL, tPLH, tPZH, tPZL, tPHZ and tPLZ must all be provided.");
    }

    if (vih == null || vil == null ||
        voh == null || vol == null)
    {
      throw new SimulatorException("VIH, VIL, VOH and VOL must all be provided.");
    }

    return new VoltageConfiguration(
        vcc,
        vil, vih,
        voh, vol,
        ns(tPHL.typicalDelay / 2),
        ns(tPHL.typicalDelay),
        ns(tPLH.typicalDelay / 2),
        ns(tPLH.typicalDelay),
        ns(tPHZ.typicalDelay / 2),
        ns(tPHZ.typicalDelay),
        ns(tPLZ.typicalDelay / 2),
        ns(tPLZ.typicalDelay),
        ns(tPZH.typicalDelay / 2),
        ns(tPZH.typicalDelay),
        ns(tPZL.typicalDelay / 2),
        ns(tPZL.typicalDelay));
  }

  static Map<String, Map<Float, List<VoltagePropagationTimeRow>>> readVoltagePropagationMap(File file)
  {
    Map<String, Map<Float, List<VoltagePropagationTimeRow>>> familyPropagationMap = new LinkedHashMap<>();  //Family -> Voltage -> Rows
    CSVFileReader csvFileReader = new CSVFileReader(file);
    CSVRow csvRow = csvFileReader.readCSVRow();
    while (csvRow != null)
    {
      String family = csvRow.getValue("Family");
      String logicLevel = csvRow.getValue("Logic Level");
      String delayType = csvRow.getValue("Propagation");
      Float vcc = parseFloat(csvRow, "VCC");
      Float typicalDelay = parseFloat(csvRow, "Typical");
      Float minimumDelay = parseFloat(csvRow, "Minimum");
      Float maximumDelay = parseFloat(csvRow, "Maximum");
      String manufacturer = csvRow.getValue("Manufacturer");
      Float vih = parseFloat(csvRow, "VIH");
      Float vil = parseFloat(csvRow, "VIL");
      Float voh = parseFloat(csvRow, "VOH");
      Float vol = parseFloat(csvRow, "VOL");

      if (StringUtil.isEmptyOrNull(family) || (vcc == null) || (vcc == 0))
      {
        throw new SimulatorException("Family and VCC must be provided.");
      }

      Map<Float, List<VoltagePropagationTimeRow>> voltagePropagationMap = familyPropagationMap.get(family);
      if (voltagePropagationMap == null)
      {
        voltagePropagationMap = new LinkedHashMap<>();
        familyPropagationMap.put(family, voltagePropagationMap);
      }

      List<VoltagePropagationTimeRow> rows = voltagePropagationMap.get(vcc);
      if (rows == null)
      {
        rows = new ArrayList<>();
        voltagePropagationMap.put(vcc, rows);
      }

      rows.add(new VoltagePropagationTimeRow(family,
                                             logicLevel,
                                             delayType,
                                             vcc,
                                             typicalDelay,
                                             minimumDelay,
                                             maximumDelay,
                                             manufacturer,
                                             vih,
                                             vil,
                                             voh,
                                             vol));

      csvRow = csvFileReader.readCSVRow();
    }
    return familyPropagationMap;
  }

  static Float parseFloat(CSVRow csvRow, String fieldName)
  {
    String value = csvRow.getValue(fieldName);
    if (StringUtil.isEmptyOrNull(value))
    {
      return null;
    }
    else
    {
      return Float.parseFloat(value);
    }
  }

  public static FamilyVoltageConfiguration get(String familyName)
  {
    Family family = FamilyStore.getInstance().get(familyName);
    return getInstance().map.get(family);
  }

  public static FamilyVoltageConfiguration get(Family family)
  {
    return get(family.getFamily());
  }

  protected static int ns(double nanoseconds)
  {
    return (int) nanosecondsToTime(nanoseconds);
  }

  public void add(VoltageConfiguration voltageConfiguration, String logicLevel, String familyName)
  {
    Family family = FamilyStore.getInstance().get(familyName);
    if (family == null)
    {
      family = FamilyStore.getInstance().add(familyName);
    }

    FamilyVoltageConfiguration familyVoltageConfiguration = map.get(family);
    if (familyVoltageConfiguration == null)
    {
      familyVoltageConfiguration = new FamilyVoltageConfiguration(family);
      map.put(family, familyVoltageConfiguration);
    }
    familyVoltageConfiguration.add(voltageConfiguration, logicLevel);
  }
}

