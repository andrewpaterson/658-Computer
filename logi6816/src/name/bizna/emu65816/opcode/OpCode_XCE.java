package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.Unsigned.toByte;

public class OpCode_XCE
    extends OpCode
{
  public OpCode_XCE(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    int cycle = cpu.getCycle();
    if (cycle == 1)
    {
      boolean oldCarry = cpu.getCpuStatus().carryFlag();
      boolean oldEmulation = cpu.getCpuStatus().emulationFlag();
      cpu.getCpuStatus().setEmulationFlag(oldCarry);
      cpu.getCpuStatus().setCarryFlag(oldEmulation);

      cpu.setX(toByte(cpu.getX()));
      cpu.setY(toByte(cpu.getY()));

      cpu.getCpuStatus().setAccumulatorWidthFlag(cpu.getCpuStatus().emulationFlag());
      cpu.getCpuStatus().setIndexWidthFlag(cpu.getCpuStatus().emulationFlag());

      cpu.clearStack();

      cpu.noAddress();
    }
    else
    {
      invalidCycle();
    }
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
    int cycle = cpu.getCycle();
    if (cycle == 1)
    {
      cpu.doneInstruction();
    }
    else
    {
      invalidCycle();
    }
  }
}

