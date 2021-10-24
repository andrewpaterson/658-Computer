package net.nexperia.logisim;

import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Value;
import net.logisim.common.LogisimPins;
import net.nexperia.lvc4245.LVC4245;
import net.nexperia.lvc4245.LVC4245Pins;

public class LVC4245LogisimPins
    extends LogisimPins
    implements LVC4245Pins
{
  protected LVC4245 transceiver;

  @Override
  public void tick()
  {
    Value outputEnabledBValue = instanceState.getPortValue(LVC4245Factory.PORT_OEB);
    Value dirValue = instanceState.getPortValue(LVC4245Factory.PORT_DIR);

    if (dirValue.isErrorValue() || dirValue.isUnknown())
    {
      instanceState.setPort(LVC4245Factory.PORT_A, Value.createUnknown(BitWidth.create(8)), 2);
      instanceState.setPort(LVC4245Factory.PORT_B, Value.createUnknown(BitWidth.create(8)), 2);
      return;
    }

    if (dirValue.equals(Value.FALSE))  //B -> A
    {
      transmit(outputEnabledBValue, LVC4245Factory.PORT_B, LVC4245Factory.PORT_A);
    }
    else
    {
      transmit(outputEnabledBValue, LVC4245Factory.PORT_A, LVC4245Factory.PORT_B);
    }
  }

  @Override
  public void setTransceiver(LVC4245 transceiver)
  {
    this.transceiver = transceiver;
  }

  private void transmit(Value outputEnabledBValue, int input, int output)
  {
    if (outputEnabledBValue.isErrorValue())
    {
      instanceState.setPort(output, Value.createUnknown(BitWidth.create(8)), 2);
    }
    else
    {
      transmit(!outputEnabledBValue.equals(Value.TRUE), input, output);
    }
  }

  private void transmit(boolean outputEnabled, int input, int output)
  {
    if (outputEnabled)
    {
      long value = instanceState.getPortValue(input).toLongValue();
      instanceState.setPort(input, Value.createUnknown(BitWidth.create(8)), 2);
      instanceState.setPort(output, Value.createKnown(BitWidth.create(8), value), 2);
    }
    else
    {
      instanceState.setPort(output, Value.createUnknown(BitWidth.create(8)), 2);
    }
  }
}

