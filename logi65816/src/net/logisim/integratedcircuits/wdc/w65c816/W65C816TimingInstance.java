package net.logisim.integratedcircuits.wdc.w65c816;

import com.cburch.logisim.instance.InstanceData;
import com.cburch.logisim.instance.InstanceState;
import net.integratedcircuits.wdc.wdc65816.W65C816Timing;
import net.logisim.common.LogiBus;
import net.logisim.common.LogiPin;

public class W65C816TimingInstance
    implements InstanceData,
               Cloneable

{
  private final W65C816Timing timing;

  public W65C816TimingInstance()
  {
    timing = new W65C816Timing();
  }

  public static W65C816TimingInstance getOrCreate(InstanceState instanceState)
  {
    W65C816TimingInstance instance = (W65C816TimingInstance) instanceState.getData();
    if (instance == null)
    {
      instance = new W65C816TimingInstance();
      instanceState.setData(instance);
    }
    return instance;
  }

  public void tick(InstanceState instanceState)
  {
    int addressStart = (int) LogiBus.getValue(W65C816TimingFactory.address.left, instanceState).getValue();
    int addressStop = (int) LogiBus.getValue(W65C816TimingFactory.address.right, instanceState).getValue();

    int readDataStart = (int) LogiBus.getValue(W65C816TimingFactory.readData.left, instanceState).getValue();
    int readDataStop = (int) LogiBus.getValue(W65C816TimingFactory.readData.right, instanceState).getValue();

    int writeDataStart = (int) LogiBus.getValue(W65C816TimingFactory.writeData.left, instanceState).getValue();
    int writeDataStop = (int) LogiBus.getValue(W65C816TimingFactory.writeData.right, instanceState).getValue();

    int bankAddressStart = (int) LogiBus.getValue(W65C816TimingFactory.bankAddress.left, instanceState).getValue();
    int bankAddressStop = (int) LogiBus.getValue(W65C816TimingFactory.bankAddress.right, instanceState).getValue();

    int interruptsStart = (int) LogiBus.getValue(W65C816TimingFactory.interrupts.left, instanceState).getValue();
    int interruptsStop = (int) LogiBus.getValue(W65C816TimingFactory.interrupts.right, instanceState).getValue();

    int abortStart = (int) LogiBus.getValue(W65C816TimingFactory.abort.left, instanceState).getValue();
    int abortStop = (int) LogiBus.getValue(W65C816TimingFactory.abort.right, instanceState).getValue();

    int mStart = (int) LogiBus.getValue(W65C816TimingFactory.m.left, instanceState).getValue();
    int mStop = (int) LogiBus.getValue(W65C816TimingFactory.m.right, instanceState).getValue();

    int xStart = (int) LogiBus.getValue(W65C816TimingFactory.x.left, instanceState).getValue();
    int xStop = (int) LogiBus.getValue(W65C816TimingFactory.x.right, instanceState).getValue();

    int eStart = (int) LogiBus.getValue(W65C816TimingFactory.e.left, instanceState).getValue();
    int eStop = (int) LogiBus.getValue(W65C816TimingFactory.e.right, instanceState).getValue();

    int phi2 = (int) LogiPin.getValue(W65C816TimingFactory.phi2, instanceState).getValue();
    int clock = (int) LogiPin.getValue(W65C816TimingFactory.clock, instanceState).getValue();

    int executeOperationInReadCycle = (int) LogiBus.getValue(W65C816TimingFactory.execute.left, instanceState).getValue();
    int executeOperationInWriteCycle = (int) LogiBus.getValue(W65C816TimingFactory.execute.right, instanceState).getValue();

    timing.set(executeOperationInReadCycle,
               executeOperationInWriteCycle,
               addressStart,
               addressStop,
               bankAddressStart,
               bankAddressStop,
               writeDataStart,
               writeDataStop,
               readDataStart,
               readDataStop,
               interruptsStart,
               interruptsStop,
               mStart,
               mStop,
               xStart,
               xStop,
               eStart,
               eStop,
               abortStart,
               abortStop);

    long t1 = timing.getT1();
    long t2 = timing.getT2();

    LogiBus.setValue(W65C816TimingFactory.timingOut1, t1, instanceState);
    LogiBus.setValue(W65C816TimingFactory.timingOut2, t2, instanceState);
  }

  @Override
  public Object clone()
  {
    try
    {
      return super.clone();
    }
    catch (CloneNotSupportedException e)
    {
      return null;
    }
  }
}

