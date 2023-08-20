package net.logicim.domain.integratedcircuit.wdc.wdc65816;

public abstract class CpuFlags
{
  public static final int STATUS_CARRY = 0x01;
  public static final int STATUS_ZERO = 0x02;
  public static final int STATUS_INTERRUPT_DISABLE = 0x04;
  public static final int STATUS_DECIMAL = 0x08;

  public static final int STATUS_BREAK = 0X10; // In emulation mode
  public static final int STATUS_INDEX_WIDTH = 0X10; // In native mode (x = 0, 16 bit)
  public static final int STATUS_ACCUMULATOR_WIDTH = 0X20;

  public static final int STATUS_OVERFLOW = 0X40;
  public static final int STATUS_NEGATIVE = 0X80;
}

