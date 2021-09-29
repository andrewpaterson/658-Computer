package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.Unsigned.toByte;

public class OpCode_REP
    extends OpCode
{
  public OpCode_REP(String name, int mCode, AddressingMode mAddressingMode)
  {
    super(name, mCode, mAddressingMode);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    int cycle = cpu.getCycle();
    switch (cycle)
    {
      case 1:
        cpu.readProgram(cpu.getAddressOfOpCodeData(getAddressingMode()));
        break;
      case 2:
        cpu.noAddress();
        break;
      default:
        invalidCycle();
        break;
    }
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
    int cycle = cpu.getCycle();
    switch (cycle)
    {
      case 1:
        int value = cpu.getPinData();
        int statusByte = cpu.getCpuStatus().getRegisterValue();
        cpu.getCpuStatus().setRegisterValue(toByte(statusByte & ~value));

        cpu.incrementProgramAddress();  //Datasheet seems to say both program counter is incremented and also not incremented.
        break;
      case 2:
        cpu.doneInstruction();
        break;
      default:
        invalidCycle();
        break;
    }
  }
}

