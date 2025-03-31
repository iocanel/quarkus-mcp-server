package io.quarkiverse.mcp.server.cli.adapter.it;

import jakarta.inject.Inject;

import io.quarkiverse.mcp.server.cli.adapter.runtime.AbstractMcpCommand;
import io.quarkus.picocli.runtime.annotations.TopCommand;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Parameters;

@TopCommand
@Command(name = "code-service", description = "Calls the code service", mixinStandardHelpOptions = true)
public class CodeServiceCommand extends AbstractMcpCommand {

    @Inject
    CodeService codeService;

    @Parameters(defaultValue = "java", description = "The lanugage.")
    String language;

    @Override
    public Integer doCall() {
        System.out.println(codeService.assist(language));
        return ExitCode.OK;
    }
}
