package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.FetchOpCodeCycles;

public class OpCode_FetchNextOpCode
    extends OpCode
{
  public OpCode_FetchNextOpCode()
  {
    super("Fetch Opcode", "Fetch Opcode", -1, new FetchOpCodeCycles());
  }
}

