package net.logisim.integratedcircuits.nexperia.lsf0204;

import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Value;
import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.nexperia.lsf0204.LSF0204;
import net.integratedcircuits.nexperia.lsf0204.LSF0204Pins;
import net.integratedcircuits.nexperia.lsf0204.LSF0204Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.nexperia.lsf0204.LSF0204Factory.*;

public class LSF0204LogisimPins
    extends LogisimPins<LSF0204Snapshot, LSF0204Pins, LSF0204>
    implements LSF0204Pins
{

  @Override
  public void startPropagation()
  {
    super.startPropagation();
    instanceState.setPort(PORT_A.index, Value.createUnknown(BitWidth.create(PORT_A.width)), PORT_A.propagationDelay);
    instanceState.setPort(PORT_B.index, Value.createUnknown(BitWidth.create(PORT_B.width)), PORT_B.propagationDelay);
  }

  @Override
  public BusValue getA()
  {
    return getValueWithoutSet(PORT_A);
  }

  @Override
  public BusValue getB()
  {
    return getValueWithoutSet(PORT_B);
  }

  @Override
  public PinValue getEN()
  {
    return getValue(PORT_EN);
  }

  @Override
  public void setA(long value)
  {
    setValue(PORT_A, value);
  }

  @Override
  public void setB(long value)
  {
    setValue(PORT_B, value);
  }
}

