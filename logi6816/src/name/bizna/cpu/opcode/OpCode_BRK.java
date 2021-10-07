package name.bizna.cpu.opcode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.addressingmode.StackSoftwareInterruptCycles;
import name.bizna.cpu.interrupt.BRKVector;

import static name.bizna.cpu.OpCodeName.BRK_Interrupt;

public class OpCode_BRK
    extends OpCode
{
  public OpCode_BRK()
  {
    super("BRK",
          "Force break software interrupt.",
          BRK_Interrupt,
          new StackSoftwareInterruptCycles(new BRKVector(), Cpu65816::BRK));
  }
}

