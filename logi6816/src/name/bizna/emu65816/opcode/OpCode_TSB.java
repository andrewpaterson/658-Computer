package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.OpCodeTable.TSB_Absolute;
import static name.bizna.emu65816.OpCodeTable.TSB_DirectPage;
import static name.bizna.emu65816.Unsigned.toByte;

public class OpCode_TSB
    extends OpCode
{
  public OpCode_TSB(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  protected void execute8BitTSB(Cpu65816 cpu)
  {
    Address addressOfOpCodeData = cpu.getAddressOfOpCodeData(getAddressingMode());
    int value = cpu.readByte(addressOfOpCodeData);
    int lowerA = Binary.lower8BitsOf(cpu.getA());
    int result = toByte(value | lowerA);
    cpu.storeByte(addressOfOpCodeData, result);
    cpu.getCpuStatus().setZeroFlag((value & lowerA) == 0);
  }

  protected void execute16BitTSB(Cpu65816 cpu)
  {
    Address addressOfOpCodeData = cpu.getAddressOfOpCodeData(getAddressingMode());
    int value = cpu.readTwoBytes(addressOfOpCodeData);
    int result = (value | cpu.getA());
    cpu.storeTwoBytes(addressOfOpCodeData, result);
    cpu.getCpuStatus().setZeroFlag((value & cpu.getA()) == 0);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    switch (getCode())
    {
      case TSB_Absolute:                 // TSB Absolute
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
      case TSB_DirectPage:                 // TSB Direct Page
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

