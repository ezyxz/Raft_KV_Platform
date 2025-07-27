package com.cuhk.raft.utils;

import com.cuhk.raft.bean.ReplicationConfig;
import com.cuhk.raft.bean.ReplicatorBean;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.*;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParseResult;

public class ToolUtils {

    public static ReplicationConfig parseReplicationConfig(String[] args) throws Exception {
        // 读取 XML 文件
        File xmlFile = new File(StringUtils.CONF_PATH);
        if (!xmlFile.exists()) {
            throw new IllegalArgumentException("XML 文件不存在：" + StringUtils.CONF_PATH);
        }

        // 使用 DocumentBuilderFactory 来解析 XML
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlFile);

        // 获取 <replication_config> 元素
        NodeList replicationConfigNodeList = document.getElementsByTagName("replication_config");
        if (replicationConfigNodeList.getLength() == 0) {
            throw new IllegalArgumentException("XML 文件格式错误，缺少 <replication_config>");
        }

        Element replicationConfigElement = (Element) replicationConfigNodeList.item(0);

        // 解析 <replication> 和 <local_replicator_id>
        int replications = Integer.parseInt(replicationConfigElement.getElementsByTagName("replication").item(0).getTextContent());
        int nodeId = Integer.parseInt(replicationConfigElement.getElementsByTagName("local_replicator_id").item(0).getTextContent());

        // 解析 <replicators> 中的每个 <replicator> 元素
        Map<Integer, ReplicatorBean> replicatorMap = new HashMap<>();
        NodeList replicatorNodeList = replicationConfigElement.getElementsByTagName("replicator");
        for (int i = 0; i < replicatorNodeList.getLength(); i++) {
            Element replicatorElement = (Element) replicatorNodeList.item(i);
            int id = Integer.parseInt(replicatorElement.getAttribute("id"));
            String address = replicatorElement.getTextContent();
            replicatorMap.put(id, new ReplicatorBean(id, address));
        }

        int args_node_id = parseNodeId(args);

        // 返回封装的 ReplicationConfig 对象
        return new ReplicationConfig(replications, args_node_id != -1 ? args_node_id : nodeId , replicatorMap);
    }

    /**
     * 内部使用的参数选项类
     */
    private static class NodeIdOptions {
        @Option(
                names = {"-i", "--nodeid"},
                description = "节点ID",
                required = true,
                paramLabel = "ID"
        )
        private Integer nodeId;
    }

    /**
     * 解析命令行参数中的节点ID
     * @param args 命令行参数数组
     * @return 解析到的节点ID
     */
    public static int parseNodeId(String[] args) {
        NodeIdOptions options = new NodeIdOptions();
        try {
            ParseResult parseResult = new CommandLine(options)
                    .setUnmatchedArgumentsAllowed(true)
                    .parseArgs(args);

            if (options.nodeId != null) {
                return options.nodeId;
            }
            return -1;
        } catch (CommandLine.ParameterException ex) {
            return -1;
        }
    }
}
