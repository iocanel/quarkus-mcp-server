package io.quarkiverse.mcp.server.cli.adapter.it;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.Callable;

import jakarta.inject.Inject;

import io.quarkiverse.mcp.server.runtime.config.McpRuntimeConfig;
import io.quarkiverse.mcp.server.stdio.runtime.StdioMcpMessageHandler;
import io.quarkus.picocli.runtime.annotations.TopCommand;
import io.quarkus.runtime.Quarkus;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Spec;

@TopCommand
@Command(name = "code-service", description = "Calls the code service", mixinStandardHelpOptions = true)
public class CodeServiceCommand implements Callable<Integer> {

    @Inject
    CodeService codeService;

    @Spec
    CommandSpec spec;

    @Parameters(defaultValue = "java", description = "The lanugage.")
    String language;

    @Option(names = { "--mcp" }, description = "Display this help message.")
    public boolean mcp;

    @Inject
    StdioMcpMessageHandler mcpMessageHandler;

    @Inject
    McpRuntimeConfig mcpRuntimeConfig;

    @Override
    public Integer call() {
        if (mcp) {
            return startMcp();
        }
        System.out.println(codeService.assist(language));
        return ExitCode.OK;
    }

    public int startMcp() {
        PrintStream stdout = System.out;
        try {
            System.setOut(new PrintStream(OutputStream.nullOutputStream()));
            mcpMessageHandler.initialize(stdout, mcpRuntimeConfig);
            Quarkus.waitForExit();
        } catch (Exception e) {
            return ExitCode.SOFTWARE;
        } finally {
            System.setOut(stdout);
        }
        return CommandLine.ExitCode.OK;
    }
}
