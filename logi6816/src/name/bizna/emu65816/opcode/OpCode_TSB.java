package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

public class OpCode_TSB
    extends OpCode
{
  public OpCode_TSB(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
  }

  protected void execute8BitTSB(Cpu65816 cpu)
  {
    Address addressOfOpCodeData = cpu.getAddressOfOpCodeData(getAddressingMode());
    byte value = cpu.readByte(addressOfOpCodeData);
    byte lowerA = Binary.lower8BitsOf(cpu.getA());
    byte result = (byte) (value | lowerA);
    cpu.storeByte(addressOfOpCodeData, result);

    if ((value & lowerA) == 0)
    {
      cpu.getCpuStatus().setZeroFlag();
    }
    else
    {
      cpu.getCpuStatus().clearZeroFlag();
    }
  }

  protected void execute16BitTSB(Cpu65816 cpu)
  {
    Address addressOfOpCodeData = cpu.getAddressOfOpCodeData(getAddressingMode());
    short value = cpu.readTwoBytes(addressOfOpCodeData);
    short result = (short) (value | cpu.getA());
    cpu.storeTwoBytes(addressOfOpCodeData, result);

    if ((value & cpu.getA()) == 0)
    {
      cpu.getCpuStatus().setZeroFlag();
    }
    else
    {
      cpu.getCpuStatus().clearZeroFlag();
    }
  }

  protected void execute8BitTRB(Cpu65816 cpu)
  {
    Address addressOfOpCodeData = cpu.getAddressOfOpCodeData(getAddressingMode());
    byte value = cpu.readByte(addressOfOpCodeData);
    byte lowerA = Binary.lower8BitsOf(cpu.getA());
    byte result = (byte) (value & ~lowerA);
    cpu.storeByte(addressOfOpCodeData, result);

    if ((value & lowerA) == 0)
    {
      cpu.getCpuStatus().setZeroFlag();
    }
    else
    {
      cpu.getCpuStatus().clearZeroFlag();
    }
  }

  protected void execute16BitTRB(Cpu65816 cpu)
  {
    Address addressOfOpCodeData = cpu.getAddressOfOpCodeData(getAddressingMode());
    short value = cpu.readTwoBytes(addressOfOpCodeData);
    short result = (short) (value & ~cpu.getA());
    cpu.storeTwoBytes(addressOfOpCodeData, result);

    if ((value & cpu.getA()) == 0)
    {
      cpu.getCpuStatus().setZeroFlag();
    }
    else
    {
      cpu.getCpuStatus().clearZeroFlag();
    }
  }

  protected void executeTSBTRB(Cpu65816 cpu)
  {
    switch (getCode())
    {
      case (0x0C):                 // TSB Absolute
      {
        if (cpu.accumulatorIs8BitWide())
        {
          execute8BitTSB(cpu);
        }
        else
        {
          execute16BitTSB(cpu);
          cpu.addToCycles(2);
        }
        cpu.addToProgramAddressAndCycles(3, 6);
        break;
      }
      case (0x04):                 // TSB Direct Page
      {
        if (cpu.accumulatorIs8BitWide())
        {
          execute8BitTSB(cpu);
        }
        else
        {
          execute16BitTSB(cpu);
          cpu.addToCycles(2);
        }
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 5);
        break;
      }
      default:
        throw new IllegalStateException("Unexpected value: " + getCode());
    }
  }
}

