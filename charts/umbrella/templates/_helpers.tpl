{{- define "kafka.fullname" -}}
{{- if .Values.fullnameOverride -}}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- $name := default .Chart.Name .Values.nameOverride -}}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" -}}
{{- end -}}
{{- end -}}


{{- define "kafka.name" -}}
{{- default .Chart.Name .Values.nameOverride -}}
{{- end -}}


{{- define "kafka.labels" -}}
app.kubernetes.io/name: {{ include "kafka.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/version: {{ .Chart.AppVersion }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
helm.sh/chart: {{ .Chart.Name }}-{{ .Chart.Version }}
{{- end -}}


{{/* Generate KRaft controller.quorum.voters string like "0@kafka-0.kafka-headless:9093,1@kafka-1.kafka-headless:9093" */}}
{{- define "kafka.kraftVoters" -}}
{{- $fullname := include "kafka.fullname" . -}}
{{- $replicas := int .Values.replicaCount -}}
{{- $port := 9093 -}}
{{- $list := list -}}
{{- range $i, $e := until $replicas -}}
{{- $entry := printf "%d@%s-%d.%s-headless:%d" $i $fullname $i $fullname $port -}}
{{- $list = append $list $entry -}}
{{- end -}}
{{- join "," $list -}}
{{- end -}}


{{/* Compute replication factors bounded by replicaCount */}}
{{- define "kafka.replicationFactor" -}}
{{- $rf := 3 -}}
{{- if lt .Values.replicaCount 3 -}}
{{- $rf = .Values.replicaCount -}}
{{- end -}}
{{- $rf -}}
{{- end -}}
