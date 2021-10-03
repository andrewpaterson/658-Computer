package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

import static name.bizna.emu65816.OpCodeName.*;
import static name.bizna.emu65816.Unsigned.toByte;

public class OpCode_TSB
    extends OpCode
{
  public OpCode_TSB(int mCode, InstructionCycles cycles)
  {
    super("TSB", "Test and Set Bit", mCode, cycles);
  }


  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    switch (getCode())
    {
      case TSB_Absolute:                 // TSB Absolute
      {
        if (cpu.isMemory8Bit())
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
        if (cpu.isMemory8Bit())
        {
          execute8BitTSB(cpu);
        }
        else
        {
          execute16BitTSB(cpu);
          cpu.addToCycles(2);
        }
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
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

