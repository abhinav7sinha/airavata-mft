# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: MFTTransferApi.proto
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import descriptor_pool as _descriptor_pool
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()


import airavata_mft_sdk.CredCommon_pb2 as CredCommon__pb2
import airavata_mft_sdk.MFTAgentStubs_pb2 as MFTAgentStubs__pb2


DESCRIPTOR = _descriptor_pool.Default().AddSerializedFile(b'\n\x14MFTTransferApi.proto\x12#org.apache.airavata.mft.api.service\x1a\x10\x43redCommon.proto\x1a\x13MFTAgentStubs.proto\"\x9b\x01\n\x10\x43\x61llbackEndpoint\x12P\n\x04type\x18\x01 \x01(\x0e\x32\x42.org.apache.airavata.mft.api.service.CallbackEndpoint.CallbackType\x12\x10\n\x08\x65ndpoint\x18\x02 \x01(\t\"#\n\x0c\x43\x61llbackType\x12\x08\n\x04HTTP\x10\x00\x12\t\n\x05KAFKA\x10\x01\"\xf3\x03\n\x12TransferApiRequest\x12\x12\n\nsourcePath\x18\x01 \x01(\t\x12\x17\n\x0fsourceStorageId\x18\x02 \x01(\t\x12\x13\n\x0bsourceToken\x18\x03 \x01(\t\x12\x17\n\x0f\x64\x65stinationPath\x18\x04 \x01(\t\x12\x1c\n\x14\x64\x65stinationStorageId\x18\x05 \x01(\t\x12\x18\n\x10\x64\x65stinationToken\x18\x06 \x01(\t\x12\x18\n\x10\x61\x66\x66inityTransfer\x18\x07 \x01(\x08\x12_\n\x0ctargetAgents\x18\x08 \x03(\x0b\x32I.org.apache.airavata.mft.api.service.TransferApiRequest.TargetAgentsEntry\x12H\n\x15mftAuthorizationToken\x18\t \x01(\x0b\x32).org.apache.airavata.mft.common.AuthToken\x12P\n\x11\x63\x61llbackEndpoints\x18\n \x03(\x0b\x32\x35.org.apache.airavata.mft.api.service.CallbackEndpoint\x1a\x33\n\x11TargetAgentsEntry\x12\x0b\n\x03key\x18\x01 \x01(\t\x12\r\n\x05value\x18\x02 \x01(\x05:\x02\x38\x01\")\n\x13TransferApiResponse\x12\x12\n\ntransferId\x18\x01 \x01(\t\"l\n\x17\x42\x61tchTransferApiRequest\x12Q\n\x10transferRequests\x18\x01 \x03(\x0b\x32\x37.org.apache.airavata.mft.api.service.TransferApiRequest\"/\n\x18\x42\x61tchTransferApiResponse\x12\x13\n\x0btransferIds\x18\x01 \x03(\t\"\xc3\x01\n\x14HttpUploadApiRequest\x12\x1c\n\x14\x64\x65stinationStorageId\x18\x01 \x01(\t\x12\x14\n\x0cresourcePath\x18\x02 \x01(\t\x12\x18\n\x10\x64\x65stinationToken\x18\x03 \x01(\t\x12\x13\n\x0btargetAgent\x18\x05 \x01(\t\x12H\n\x15mftAuthorizationToken\x18\x06 \x01(\x0b\x32).org.apache.airavata.mft.common.AuthToken\"9\n\x15HttpUploadApiResponse\x12\x0b\n\x03url\x18\x01 \x01(\t\x12\x13\n\x0btargetAgent\x18\x02 \x01(\t\"\xbb\x01\n\x16HttpDownloadApiRequest\x12\x14\n\x0cresourcePath\x18\x01 \x01(\t\x12\x17\n\x0fsourceStorageId\x18\x02 \x01(\t\x12\x13\n\x0bsourceToken\x18\x03 \x01(\t\x12\x13\n\x0btargetAgent\x18\x05 \x01(\t\x12H\n\x15mftAuthorizationToken\x18\x06 \x01(\x0b\x32).org.apache.airavata.mft.common.AuthToken\";\n\x17HttpDownloadApiResponse\x12\x0b\n\x03url\x18\x01 \x01(\t\x12\x13\n\x0btargetAgent\x18\x02 \x01(\t\"w\n\x17TransferStateApiRequest\x12\x12\n\ntransferId\x18\x01 \x01(\t\x12H\n\x15mftAuthorizationToken\x18\x02 \x01(\x0b\x32).org.apache.airavata.mft.common.AuthToken\"j\n\x18TransferStateApiResponse\x12\r\n\x05state\x18\x01 \x01(\t\x12\x16\n\x0eupdateTimeMils\x18\x02 \x01(\x03\x12\x12\n\npercentage\x18\x03 \x01(\x01\x12\x13\n\x0b\x64\x65scription\x18\x04 \x01(\t\"1\n\x1cResourceAvailabilityResponse\x12\x11\n\tavailable\x18\x01 \x01(\x08\"^\n!GetResourceMetadataFromIDsRequest\x12\x14\n\x0cresourcePath\x18\x01 \x01(\t\x12\x11\n\tstorageId\x18\x02 \x01(\t\x12\x10\n\x08secretId\x18\x03 \x01(\t\"\xa9\x02\n\x1c\x46\x65tchResourceMetadataRequest\x12W\n\rdirectRequest\x18\x01 \x01(\x0b\x32>.org.apache.airavata.mft.agent.stub.GetResourceMetadataRequestH\x00\x12[\n\tidRequest\x18\x02 \x01(\x0b\x32\x46.org.apache.airavata.mft.api.service.GetResourceMetadataFromIDsRequestH\x00\x12H\n\x15mftAuthorizationToken\x18\x03 \x01(\x0b\x32).org.apache.airavata.mft.common.AuthTokenB\t\n\x07request2\xa4\t\n\x12MFTTransferService\x12\x83\x01\n\x0esubmitTransfer\x12\x37.org.apache.airavata.mft.api.service.TransferApiRequest\x1a\x38.org.apache.airavata.mft.api.service.TransferApiResponse\x12\x92\x01\n\x13submitBatchTransfer\x12<.org.apache.airavata.mft.api.service.BatchTransferApiRequest\x1a=.org.apache.airavata.mft.api.service.BatchTransferApiResponse\x12\x89\x01\n\x10submitHttpUpload\x12\x39.org.apache.airavata.mft.api.service.HttpUploadApiRequest\x1a:.org.apache.airavata.mft.api.service.HttpUploadApiResponse\x12\x8f\x01\n\x12submitHttpDownload\x12;.org.apache.airavata.mft.api.service.HttpDownloadApiRequest\x1a<.org.apache.airavata.mft.api.service.HttpDownloadApiResponse\x12\x92\x01\n\x11getTransferStates\x12<.org.apache.airavata.mft.api.service.TransferStateApiRequest\x1a=.org.apache.airavata.mft.api.service.TransferStateApiResponse0\x01\x12\x8f\x01\n\x10getTransferState\x12<.org.apache.airavata.mft.api.service.TransferStateApiRequest\x1a=.org.apache.airavata.mft.api.service.TransferStateApiResponse\x12\x9f\x01\n\x17getResourceAvailability\x12\x41.org.apache.airavata.mft.api.service.FetchResourceMetadataRequest\x1a\x41.org.apache.airavata.mft.api.service.ResourceAvailabilityResponse\x12\x8b\x01\n\x10resourceMetadata\x12\x41.org.apache.airavata.mft.api.service.FetchResourceMetadataRequest\x1a\x34.org.apache.airavata.mft.agent.stub.ResourceMetadataB\x02P\x01\x62\x06proto3')



_CALLBACKENDPOINT = DESCRIPTOR.message_types_by_name['CallbackEndpoint']
_TRANSFERAPIREQUEST = DESCRIPTOR.message_types_by_name['TransferApiRequest']
_TRANSFERAPIREQUEST_TARGETAGENTSENTRY = _TRANSFERAPIREQUEST.nested_types_by_name['TargetAgentsEntry']
_TRANSFERAPIRESPONSE = DESCRIPTOR.message_types_by_name['TransferApiResponse']
_BATCHTRANSFERAPIREQUEST = DESCRIPTOR.message_types_by_name['BatchTransferApiRequest']
_BATCHTRANSFERAPIRESPONSE = DESCRIPTOR.message_types_by_name['BatchTransferApiResponse']
_HTTPUPLOADAPIREQUEST = DESCRIPTOR.message_types_by_name['HttpUploadApiRequest']
_HTTPUPLOADAPIRESPONSE = DESCRIPTOR.message_types_by_name['HttpUploadApiResponse']
_HTTPDOWNLOADAPIREQUEST = DESCRIPTOR.message_types_by_name['HttpDownloadApiRequest']
_HTTPDOWNLOADAPIRESPONSE = DESCRIPTOR.message_types_by_name['HttpDownloadApiResponse']
_TRANSFERSTATEAPIREQUEST = DESCRIPTOR.message_types_by_name['TransferStateApiRequest']
_TRANSFERSTATEAPIRESPONSE = DESCRIPTOR.message_types_by_name['TransferStateApiResponse']
_RESOURCEAVAILABILITYRESPONSE = DESCRIPTOR.message_types_by_name['ResourceAvailabilityResponse']
_GETRESOURCEMETADATAFROMIDSREQUEST = DESCRIPTOR.message_types_by_name['GetResourceMetadataFromIDsRequest']
_FETCHRESOURCEMETADATAREQUEST = DESCRIPTOR.message_types_by_name['FetchResourceMetadataRequest']
_CALLBACKENDPOINT_CALLBACKTYPE = _CALLBACKENDPOINT.enum_types_by_name['CallbackType']
CallbackEndpoint = _reflection.GeneratedProtocolMessageType('CallbackEndpoint', (_message.Message,), {
  'DESCRIPTOR' : _CALLBACKENDPOINT,
  '__module__' : 'MFTTransferApi_pb2'
  # @@protoc_insertion_point(class_scope:org.apache.airavata.mft.api.service.CallbackEndpoint)
  })
_sym_db.RegisterMessage(CallbackEndpoint)

TransferApiRequest = _reflection.GeneratedProtocolMessageType('TransferApiRequest', (_message.Message,), {

  'TargetAgentsEntry' : _reflection.GeneratedProtocolMessageType('TargetAgentsEntry', (_message.Message,), {
    'DESCRIPTOR' : _TRANSFERAPIREQUEST_TARGETAGENTSENTRY,
    '__module__' : 'MFTTransferApi_pb2'
    # @@protoc_insertion_point(class_scope:org.apache.airavata.mft.api.service.TransferApiRequest.TargetAgentsEntry)
    })
  ,
  'DESCRIPTOR' : _TRANSFERAPIREQUEST,
  '__module__' : 'MFTTransferApi_pb2'
  # @@protoc_insertion_point(class_scope:org.apache.airavata.mft.api.service.TransferApiRequest)
  })
_sym_db.RegisterMessage(TransferApiRequest)
_sym_db.RegisterMessage(TransferApiRequest.TargetAgentsEntry)

TransferApiResponse = _reflection.GeneratedProtocolMessageType('TransferApiResponse', (_message.Message,), {
  'DESCRIPTOR' : _TRANSFERAPIRESPONSE,
  '__module__' : 'MFTTransferApi_pb2'
  # @@protoc_insertion_point(class_scope:org.apache.airavata.mft.api.service.TransferApiResponse)
  })
_sym_db.RegisterMessage(TransferApiResponse)

BatchTransferApiRequest = _reflection.GeneratedProtocolMessageType('BatchTransferApiRequest', (_message.Message,), {
  'DESCRIPTOR' : _BATCHTRANSFERAPIREQUEST,
  '__module__' : 'MFTTransferApi_pb2'
  # @@protoc_insertion_point(class_scope:org.apache.airavata.mft.api.service.BatchTransferApiRequest)
  })
_sym_db.RegisterMessage(BatchTransferApiRequest)

BatchTransferApiResponse = _reflection.GeneratedProtocolMessageType('BatchTransferApiResponse', (_message.Message,), {
  'DESCRIPTOR' : _BATCHTRANSFERAPIRESPONSE,
  '__module__' : 'MFTTransferApi_pb2'
  # @@protoc_insertion_point(class_scope:org.apache.airavata.mft.api.service.BatchTransferApiResponse)
  })
_sym_db.RegisterMessage(BatchTransferApiResponse)

HttpUploadApiRequest = _reflection.GeneratedProtocolMessageType('HttpUploadApiRequest', (_message.Message,), {
  'DESCRIPTOR' : _HTTPUPLOADAPIREQUEST,
  '__module__' : 'MFTTransferApi_pb2'
  # @@protoc_insertion_point(class_scope:org.apache.airavata.mft.api.service.HttpUploadApiRequest)
  })
_sym_db.RegisterMessage(HttpUploadApiRequest)

HttpUploadApiResponse = _reflection.GeneratedProtocolMessageType('HttpUploadApiResponse', (_message.Message,), {
  'DESCRIPTOR' : _HTTPUPLOADAPIRESPONSE,
  '__module__' : 'MFTTransferApi_pb2'
  # @@protoc_insertion_point(class_scope:org.apache.airavata.mft.api.service.HttpUploadApiResponse)
  })
_sym_db.RegisterMessage(HttpUploadApiResponse)

HttpDownloadApiRequest = _reflection.GeneratedProtocolMessageType('HttpDownloadApiRequest', (_message.Message,), {
  'DESCRIPTOR' : _HTTPDOWNLOADAPIREQUEST,
  '__module__' : 'MFTTransferApi_pb2'
  # @@protoc_insertion_point(class_scope:org.apache.airavata.mft.api.service.HttpDownloadApiRequest)
  })
_sym_db.RegisterMessage(HttpDownloadApiRequest)

HttpDownloadApiResponse = _reflection.GeneratedProtocolMessageType('HttpDownloadApiResponse', (_message.Message,), {
  'DESCRIPTOR' : _HTTPDOWNLOADAPIRESPONSE,
  '__module__' : 'MFTTransferApi_pb2'
  # @@protoc_insertion_point(class_scope:org.apache.airavata.mft.api.service.HttpDownloadApiResponse)
  })
_sym_db.RegisterMessage(HttpDownloadApiResponse)

TransferStateApiRequest = _reflection.GeneratedProtocolMessageType('TransferStateApiRequest', (_message.Message,), {
  'DESCRIPTOR' : _TRANSFERSTATEAPIREQUEST,
  '__module__' : 'MFTTransferApi_pb2'
  # @@protoc_insertion_point(class_scope:org.apache.airavata.mft.api.service.TransferStateApiRequest)
  })
_sym_db.RegisterMessage(TransferStateApiRequest)

TransferStateApiResponse = _reflection.GeneratedProtocolMessageType('TransferStateApiResponse', (_message.Message,), {
  'DESCRIPTOR' : _TRANSFERSTATEAPIRESPONSE,
  '__module__' : 'MFTTransferApi_pb2'
  # @@protoc_insertion_point(class_scope:org.apache.airavata.mft.api.service.TransferStateApiResponse)
  })
_sym_db.RegisterMessage(TransferStateApiResponse)

ResourceAvailabilityResponse = _reflection.GeneratedProtocolMessageType('ResourceAvailabilityResponse', (_message.Message,), {
  'DESCRIPTOR' : _RESOURCEAVAILABILITYRESPONSE,
  '__module__' : 'MFTTransferApi_pb2'
  # @@protoc_insertion_point(class_scope:org.apache.airavata.mft.api.service.ResourceAvailabilityResponse)
  })
_sym_db.RegisterMessage(ResourceAvailabilityResponse)

GetResourceMetadataFromIDsRequest = _reflection.GeneratedProtocolMessageType('GetResourceMetadataFromIDsRequest', (_message.Message,), {
  'DESCRIPTOR' : _GETRESOURCEMETADATAFROMIDSREQUEST,
  '__module__' : 'MFTTransferApi_pb2'
  # @@protoc_insertion_point(class_scope:org.apache.airavata.mft.api.service.GetResourceMetadataFromIDsRequest)
  })
_sym_db.RegisterMessage(GetResourceMetadataFromIDsRequest)

FetchResourceMetadataRequest = _reflection.GeneratedProtocolMessageType('FetchResourceMetadataRequest', (_message.Message,), {
  'DESCRIPTOR' : _FETCHRESOURCEMETADATAREQUEST,
  '__module__' : 'MFTTransferApi_pb2'
  # @@protoc_insertion_point(class_scope:org.apache.airavata.mft.api.service.FetchResourceMetadataRequest)
  })
_sym_db.RegisterMessage(FetchResourceMetadataRequest)

_MFTTRANSFERSERVICE = DESCRIPTOR.services_by_name['MFTTransferService']
if _descriptor._USE_C_DESCRIPTORS == False:

  DESCRIPTOR._options = None
  DESCRIPTOR._serialized_options = b'P\001'
  _TRANSFERAPIREQUEST_TARGETAGENTSENTRY._options = None
  _TRANSFERAPIREQUEST_TARGETAGENTSENTRY._serialized_options = b'8\001'
  _CALLBACKENDPOINT._serialized_start=101
  _CALLBACKENDPOINT._serialized_end=256
  _CALLBACKENDPOINT_CALLBACKTYPE._serialized_start=221
  _CALLBACKENDPOINT_CALLBACKTYPE._serialized_end=256
  _TRANSFERAPIREQUEST._serialized_start=259
  _TRANSFERAPIREQUEST._serialized_end=758
  _TRANSFERAPIREQUEST_TARGETAGENTSENTRY._serialized_start=707
  _TRANSFERAPIREQUEST_TARGETAGENTSENTRY._serialized_end=758
  _TRANSFERAPIRESPONSE._serialized_start=760
  _TRANSFERAPIRESPONSE._serialized_end=801
  _BATCHTRANSFERAPIREQUEST._serialized_start=803
  _BATCHTRANSFERAPIREQUEST._serialized_end=911
  _BATCHTRANSFERAPIRESPONSE._serialized_start=913
  _BATCHTRANSFERAPIRESPONSE._serialized_end=960
  _HTTPUPLOADAPIREQUEST._serialized_start=963
  _HTTPUPLOADAPIREQUEST._serialized_end=1158
  _HTTPUPLOADAPIRESPONSE._serialized_start=1160
  _HTTPUPLOADAPIRESPONSE._serialized_end=1217
  _HTTPDOWNLOADAPIREQUEST._serialized_start=1220
  _HTTPDOWNLOADAPIREQUEST._serialized_end=1407
  _HTTPDOWNLOADAPIRESPONSE._serialized_start=1409
  _HTTPDOWNLOADAPIRESPONSE._serialized_end=1468
  _TRANSFERSTATEAPIREQUEST._serialized_start=1470
  _TRANSFERSTATEAPIREQUEST._serialized_end=1589
  _TRANSFERSTATEAPIRESPONSE._serialized_start=1591
  _TRANSFERSTATEAPIRESPONSE._serialized_end=1697
  _RESOURCEAVAILABILITYRESPONSE._serialized_start=1699
  _RESOURCEAVAILABILITYRESPONSE._serialized_end=1748
  _GETRESOURCEMETADATAFROMIDSREQUEST._serialized_start=1750
  _GETRESOURCEMETADATAFROMIDSREQUEST._serialized_end=1844
  _FETCHRESOURCEMETADATAREQUEST._serialized_start=1847
  _FETCHRESOURCEMETADATAREQUEST._serialized_end=2144
  _MFTTRANSFERSERVICE._serialized_start=2147
  _MFTTRANSFERSERVICE._serialized_end=3335
# @@protoc_insertion_point(module_scope)
