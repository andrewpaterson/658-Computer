package net.logicim.domain.common.propagation;

public class VoltagePropagationTimeRow
{
  public String family;
  public Float vcc;

  public String delayType;
  public Float typicalDelay;
  public Float minimumDelay;
  public Float maximumDelay;

  public Float vih;
  public Float vil;
  public Float voh;
  public Float vol;

  public String logicLevel;
  public String manufacturer;

  public VoltagePropagationTimeRow(String family,
                                   String logicLevel,
                                   String delayType,
                                   Float vcc,
                                   Float typicalDelay,
                                   Float minimumDelay,
                                   Float maximumDelay,
                                   String manufacturer,
                                   Float vih,
                                   Float vil,
                                   Float voh,
                                   Float vol)
  {
    this.family = family;
    this.logicLevel = logicLevel;
    this.delayType = delayType;
    this.vcc = vcc;
    this.typicalDelay = typicalDelay;
    this.minimumDelay = minimumDelay;
    this.maximumDelay = maximumDelay;
    this.manufacturer = manufacturer;
    this.vih = vih;
    this.vil = vil;
    this.voh = voh;
    this.vol = vol;
  }
}

